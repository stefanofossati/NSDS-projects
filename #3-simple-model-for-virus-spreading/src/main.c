#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>
#include <time.h>
#include <string.h>

#include "utils/read_file.h"
#include "utils/init_config.h"
#include "utils/linked_list.h"
#include "utils/mpi_datatypes.h"
#include "utils/person.h"
#include "utils/country.h"

#define LEADER 0
#define TOTAL_TIME 30000000
#define PRIORITY_LOG_LEVEL 1

/* Function prototypes */

person_t *merge_people_arrays(person_t *infected_people, person_t *received_infected_people, int infected_people_size, int received_infected_people_size);

int main(int argc, char **argv) {

    /*volatile int i = 0;
    char hostname[256];
    gethostname(hostname, sizeof(hostname));
    printf("PID %d on %s ready for attach\n", getpid(), hostname);
    fflush(stdout);
    while (0 == i)
        sleep(5);
    */
    MPI_Init(NULL, NULL);

    int my_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Create MPI_Datatypes
    MPI_Datatype MPI_INIT_CONFIG = create_mpi_init_config();
    MPI_Datatype MPI_POSITION = create_mpi_position();
    MPI_Datatype MPI_PERSON = create_mpi_person(MPI_POSITION);
    MPI_Datatype MPI_COUNTRY_NUMBER = create_mpi_country_number();

    file_parameters_t param;
    init_config_t init_config;
    if(my_rank == LEADER){
        log_message(INFO_1,"start initialization\n", PRIORITY_LOG_LEVEL);
        param = get_parameters_from_file("config.txt", PRIORITY_LOG_LEVEL);

        if(!check_parameters(&param)){
            log_message(ERROR, "Error in parameters", PRIORITY_LOG_LEVEL);
            MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            return 1;
        }else {
            log_message(INFO_1, "Parameters are correct\n", PRIORITY_LOG_LEVEL);
        }
        printf("parameters are: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d, v = %f, d = %d, t = %d\n", param.N, param.I, param.W, param.L, param.w, param.l, param.v, param.d, param.t);
        //Maybe check in the number of individuals is divisible by the number of processes

        init_config = set_init_config((int)param.N/world_size, (int)param.I/world_size, param.W, param.L, param.w, param.l, param.v, param.d, param.t);
    }

    // Broadcast the configuration to all processes
    MPI_Bcast(&init_config, 1, MPI_INIT_CONFIG, 0, MPI_COMM_WORLD);

    if(my_rank == LEADER) {
        init_config = set_init_config((int) param.N / world_size + param.N % world_size,
                                      (int) param.I / world_size + param.I % world_size, param.W, param.L, param.w,
                                      param.l, param.v, param.d, param.t);
    }
    printf("My rank % d, init_config: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d, d = %d, v = %d, t = %d\n", my_rank, init_config.total_people, init_config.infected_people, init_config.W, init_config.L, init_config.w, init_config.l, init_config.v, init_config.d, init_config.t);
    srand(time(NULL) + my_rank);

    /* Create the linked list of people */
    linked_list_t *non_infected_list = create_people_linked_list(init_config.total_people - init_config.infected_people, NON_INFECTED, init_config.W, init_config.L);
    linked_list_t *infected_list = create_people_linked_list(init_config.infected_people, INFECTED, init_config.W, init_config.L);


    int num_of_countries;
    num_of_countries = (init_config.W/init_config.w) * (init_config.L/init_config.l);
    
    //printf("my rank: %d, non-infected list length: %d, infected list length: %d\n", my_rank, get_linked_list_length(non_infected_list), get_linked_list_length(infected_list));

    //printf("my rank: %d, position a: x=%f, y=%f\n", my_rank, non_infected_list->head->person->position.x, non_infected_list->head->person->position.y);
    //printf("my rank: %d, position b: x=%f, y=%f\n", my_rank, non_infected_list->head->next->person->position.x, non_infected_list->head->next->person->position.y);

    int current_time = 0;
    int day_time = 0;

    while(current_time < TOTAL_TIME){
        // update people position
        update_position_list(&init_config, non_infected_list);
        update_position_list(&init_config, infected_list);

        // Convert the linked list to an array
        person_t *infected_array = linked_list_to_array(infected_list);

        person_t *received_infected_array;

        int number_amount;
        int infected_num;

        // TO VERIFY WHETHER IT IS NECESSARY
        MPI_Barrier(MPI_COMM_WORLD);

        if(my_rank == LEADER){
            int i = 1;
            received_infected_array = (person_t *)calloc(init_config.total_people, sizeof(person_t));
            infected_num = get_linked_list_length(infected_list);
            received_infected_array = merge_people_arrays(received_infected_array, infected_array, 0, infected_num);

            //printf("I'm process %d and I'm starting collecting infected'.\n", my_rank);

            while(i<world_size){
                MPI_Status status;

                MPI_Probe(i, 0, MPI_COMM_WORLD, &status);

                // When probe returns, the status object has the size and other
                // attributes of the incoming message. Get the message size
                MPI_Get_count(&status, MPI_PERSON, &number_amount);

                // Allocate a buffer to hold the incoming people
                person_t *people_buf = (person_t *)calloc(number_amount, sizeof(person_t));

                // Now receive the message with the allocated buffer
                MPI_Recv(people_buf, number_amount, MPI_PERSON, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                //printf("Position first received: x=%f, y=%f\n", people_buf[0].position.x, people_buf[0].position.y);

                // Merge the received people with the infected array
                received_infected_array = merge_people_arrays(received_infected_array, people_buf, infected_num, number_amount);
                infected_num += number_amount;

                //printf("Leader dynamically received %d infected people from %d at time %d.\n", number_amount, i, current_time);
                // TO VERIFY WHETHER THE FREE IS LEGITIMATE
                free(people_buf);
                i++;
            }

            printf("Leader has %d infected people at time %d.\n", infected_num, current_time);

            // DEBUG ONLY
            // printf("Position first infected: x=%f, y=%f\n", received_infected_array[0].position.x, received_infected_array[0].position.y);
            // printf("Position second infected: x=%f, y=%f\n", received_infected_array[1].position.x, received_infected_array[1].position.y);
            // printf("Position third infected: x=%f, y=%f\n", received_infected_array[2].position.x, received_infected_array[2].position.y);
            // printf("Position fourth infected: x=%f, y=%f\n", received_infected_array[3].position.x, received_infected_array[3].position.y);
            // printf("Position fifth infected: x=%f, y=%f\n", received_infected_array[4].position.x, received_infected_array[4].position.y);
            // printf("Position sixth infected: x=%f, y=%f\n", received_infected_array[5].position.x, received_infected_array[5].position.y);
            // printf("Position seventh infected: x=%f, y=%f\n", received_infected_array[6].position.x, received_infected_array[6].position.y);
            // printf("Position tenth infected: x=%f, y=%f\n", received_infected_array[9].position.x, received_infected_array[9].position.y);
            // printf("Size of person_t: %d\n", sizeof(person_t));
        } else{
            number_amount = get_linked_list_length(infected_list);
            // Send the infected people array to the leader
            MPI_Send(infected_array, number_amount, MPI_PERSON, 0, 0, MPI_COMM_WORLD);
            // printf("%d sent %d infected people to to leader\n", my_rank, number_amount);
        }

        //printf("I'm process %d and I am alive before broadcast.\n", my_rank);

        MPI_Barrier(MPI_COMM_WORLD);

        // Send the number of infected people to all processes
        MPI_Bcast(&infected_num, 1, MPI_INT, 0, MPI_COMM_WORLD);

        if(my_rank != LEADER){
            // Allocate a buffer to hold the incoming people
            received_infected_array = (person_t *)calloc(infected_num, sizeof(person_t));
        }

        // Send the merged array of infected people to all processes
        MPI_Bcast(received_infected_array, infected_num, MPI_PERSON, 0, MPI_COMM_WORLD);

        //printf("I'm process %d and I am alive after broadcast.\n", my_rank);

        // TO VERIFY WHETHER THEY ARE LEGITIMATE    
        free(infected_array);  // I can free because I don't need infected_array

        // Update non infected people list status
        node_t *current_non_infected_node = non_infected_list->head;
        while(current_non_infected_node != NULL){
            // update status immune people
            if(current_non_infected_node->person->status == IMMUNE){
                current_non_infected_node->person->status_timer -= init_config.t;  // decrement timer of immune people
                change_status(current_non_infected_node->person);  //check if the status has to be changed
            }else if(current_non_infected_node->person->status == NON_INFECTED){
                // update status non infected people
                bool is_being_infected = false;
                for(int k=0; k<infected_num; k++){  // check that the non infected people are near an almost one infected person
                    if(infected_is_near(current_non_infected_node->person, &received_infected_array[k], init_config.d)){
                        is_being_infected = true;
                        current_non_infected_node->person->status_timer -= init_config.t; // decrement timer of non infected people
                        change_status(current_non_infected_node->person);   //check if the status has to be changed
                        break;  // once an infected person is near the non infected, I can break the loop
                    }
                }
                if(!is_being_infected){
                    reset_non_infected_timer(current_non_infected_node->person); // reset timer of non infected people
                }   
            }
            current_non_infected_node = current_non_infected_node->next; // go to the next non infected person
        }

        //change infected people list status
        node_t *current_infected_node = infected_list->head;
        // check if the infected people have to change status
        while(current_infected_node!=NULL){
            if(current_infected_node->person->status == INFECTED){
                current_infected_node->person->status_timer -= init_config.t;
                change_status(current_infected_node->person);
            }
            current_infected_node = current_infected_node->next;
        }

        // DEBUG ONLY 
        // printf("I'm process %d and I am alive after updating status.\n", my_rank);

        // Move infected people from non_infected list to infected_list
        move_people_in_list(non_infected_list, infected_list, INFECTED);

        // DEBUG ONLY
        // printf("I'm process %d and I am alive after moving non_infected to infected list.\n", my_rank);

        //Move immune people from infected_list to non_infected_list
        move_people_in_list(infected_list, non_infected_list, IMMUNE);

        // DEBUG ONLY
        // printf("I'm process %d and I am alive after moving immune to non_infected list.\n", my_rank);

        /* DEBUG ONLY
        printf("The processes must be updated.\n");
        for(int j=0; j< world_size; j++){
            printf("Process %d, infected_list: %d\n", my_rank, get_linked_list_length(infected_list));
        } */

        free(received_infected_array);

        if(day_time >= 24*60*60){  // probabilmente bisogna pensare di avere un init_config_t divisibile per un giorno;
            day_time = 0;
            country_number_t countries[init_config.W/init_config.w][init_config.L/init_config.l];

            // DEBUG ONLY
            printf("I'm process %d and I am alive before computing people in countries.\n", my_rank);

            for(node_t *node_person = non_infected_list->head; node_person != NULL; node_person = node_person->next){
                person_in_country(node_person->person, init_config.W, init_config.L, init_config.w, init_config.l, countries);
            }

            for(node_t *node_person = infected_list->head; node_person != NULL; node_person = node_person->next){
                person_in_country(node_person->person, init_config.W, init_config.L, init_config.w, init_config.l, countries);
            }

            // DEBUG ONLY
            printf("I'm process %d and I am alive after computing people in countries.\n", my_rank);

            if(my_rank == LEADER) {
                int i = 1;
                country_number_t *country_file_array = calloc(num_of_countries, sizeof(country_number_t));
                country_file_array = convert_to_array(init_config.W/init_config.w, init_config.L/init_config.l, country_file_array, countries);
                while (i < world_size) {
                    //receive the country array from the other processes
                    country_number_t *received_countries = calloc(num_of_countries, sizeof(country_number_t));
                    MPI_Recv(received_countries, num_of_countries, MPI_COUNTRY_NUMBER, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                    merge_country_arrays(country_file_array, received_countries, num_of_countries);
                    free(received_countries);
                    i++;
                }

                write_on_file("results.csv", country_file_array, num_of_countries);

                free(country_file_array);
            }else{
                //send the country array to the leader
                country_number_t *country_array_to_send = calloc(num_of_countries, sizeof(country_number_t));
                country_array_to_send = convert_to_array(init_config.W/init_config.w, init_config.L/init_config.l, country_array_to_send, countries);

                MPI_Send(country_array_to_send, (init_config.W/init_config.w) * (init_config.L/init_config.l), MPI_COUNTRY_NUMBER, LEADER, 0, MPI_COMM_WORLD);

                free(country_array_to_send);
            }

            MPI_Barrier(MPI_COMM_WORLD);
        }


        current_time += init_config.t;
        day_time += init_config.t;

        MPI_Barrier(MPI_COMM_WORLD);
    }

    // printf("POST: my rank: %d, position a: x=%f, y=%f\n", my_rank, non_infected_list->head->person->position.x, non_infected_list->head->person->position.y);
    // printf("POST: my rank: %d, position b: x=%f, y=%f\n", my_rank, non_infected_list->head->next->person->position.x, non_infected_list->head->next->person->position.y);

    MPI_Type_free(&MPI_INIT_CONFIG);
    MPI_Type_free(&MPI_POSITION);
    MPI_Type_free(&MPI_PERSON);
    MPI_Finalize();
}

person_t *merge_people_arrays(person_t *infected_people, person_t *received_infected_people, int infected_people_size, int received_infected_people_size){
    person_t *merged_people = (person_t *)calloc((infected_people_size + received_infected_people_size), sizeof(person_t));
    
    if(merged_people == NULL){
        printf("Error allocating memory for merged people array\n");
        MPI_Abort(MPI_COMM_WORLD, 1);
    }
    memcpy(merged_people, infected_people, infected_people_size * sizeof(person_t));
    memcpy(merged_people + infected_people_size, received_infected_people, received_infected_people_size * sizeof(person_t));

    free(infected_people);
    return merged_people;
}