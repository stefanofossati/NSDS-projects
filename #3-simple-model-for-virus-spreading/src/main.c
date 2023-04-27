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

#define LEADER 0
#define TOTAL_TIME 100000000

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

    file_parameters_t param;
    init_config_t init_config;
    if(my_rank == LEADER){
        printf("start initializaion\n");
        param = get_parameters_from_file("../config.txt");

        if(!check_parameters(&param)){
            printf("Error in parameters");
            MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            return 1;
        }else {
            printf("Parameters are correct\n");
        }
        printf("parameters are: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d, v = %d, d = %d, t = %d\n", param.N, param.I, param.W, param.L, param.w, param.l, param.v, param.d, param.t);
        //Maybe check in the number of individuals is divisible by the number of processes

        init_config = set_init_config((int)param.N/world_size, (int)param.I/world_size, param.W, param.L, param.w, param.l, param.v, param.d, param.t);
        }

    /* Broadcast the configuration to all processes */
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

    printf("my rank: %d, non-infected list length: %d, infected list length: %d\n", my_rank, get_linked_list_length(non_infected_list),
           get_linked_list_length(infected_list));
    int current_time = 0;

    printf("my rank: %d, position a: x=%f, y=%f\n", my_rank, non_infected_list->head->person->position.x, non_infected_list->head->person->position.y);
    printf("my rank: %d, position b: x=%f, y=%f\n", my_rank, non_infected_list->head->next->person->position.x, non_infected_list->head->next->person->position.y);

    while(current_time < TOTAL_TIME){
        // update people position
        update_position_list(&init_config, non_infected_list);
        update_position_list(&init_config, infected_list);

        // Convert the linked list to an array
        person_t *infected_array = linked_list_to_array(infected_list);

        int number_amount;
        int infected_num;

        if(my_rank == LEADER){
            int i = 1;
            infected_num = get_linked_list_length(infected_list);
            while(i<world_size){
                MPI_Status status;

                MPI_Probe(i, 0, MPI_COMM_WORLD, &status);

                // When probe returns, the status object has the size and other
                // attributes of the incoming message. Get the message size
                MPI_Get_count(&status, MPI_PERSON, &number_amount);

                // Allocate a buffer to hold the incoming people
                person_t *people_buf = (person_t *)malloc(number_amount * sizeof(person_t));

                // Now receive the message with the allocated buffer
                MPI_Recv(people_buf, number_amount, MPI_PERSON, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                // Merge the received people with the infected array
                infected_array = merge_people_arrays(infected_array, people_buf, infected_num, number_amount);
                infected_num += number_amount;

                printf("Leader dynamically received %d infected people from %d at time %d.\n", number_amount, i, current_time);
                free(people_buf);
                i++;
            }

            printf("Leader has %d infected people at time %d.\n", infected_num, current_time);
        } else{
            number_amount = get_linked_list_length(infected_list);
            // Send the infected people array to the leader
            MPI_Send(infected_array, number_amount, MPI_PERSON, 0, 0, MPI_COMM_WORLD);
            // printf("%d sent %d infected people to to leader\n", my_rank, number_amount);
        }

        // Send the merged array of infected people to all processes
        // MPI_Bcast(infected_array, infected_num, MPI_PERSON, 0, MPI_COMM_WORLD);

        free(infected_array);
        current_time += init_config.t;
    }
    // printf("POST: my rank: %d, position a: x=%f, y=%f\n", my_rank, non_infected_list->head->person->position.x, non_infected_list->head->person->position.y);
    // printf("POST: my rank: %d, position b: x=%f, y=%f\n", my_rank, non_infected_list->head->next->person->position.x, non_infected_list->head->next->person->position.y);


    MPI_Type_free(&MPI_INIT_CONFIG);
    MPI_Type_free(&MPI_POSITION);
    MPI_Type_free(&MPI_PERSON);
    MPI_Finalize();
}

person_t *merge_people_arrays(person_t *infected_people, person_t *received_infected_people, int infected_people_size, int received_infected_people_size){
    person_t *merged_people = (person_t *)malloc((infected_people_size + received_infected_people_size) * sizeof(person_t));
    
    memcpy(merged_people, infected_people, infected_people_size * sizeof(person_t));
    memcpy(merged_people + infected_people_size, received_infected_people, received_infected_people_size * sizeof(person_t));

    free(infected_people);
    return merged_people;
}