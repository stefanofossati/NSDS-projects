#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>
#include <time.h>

#include "utils/read_file.h"
#include "utils/init_config.h"
#include "utils/linked_list.h"
#include "utils/mpi_datatypes.h"
#include "utils/person.h"

#define LEADER 0
#define TOTAL_TIME 10

/* Function prototypes */



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
    MPI_Datatype mpi_init_config = create_mpi_init_config();

    file_parameters_t param;
    init_config_t init_config;
    if(my_rank == LEADER){
        printf("start initializaion\n");
        param = get_parameters_form_file("../config.txt");

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
    MPI_Bcast(&init_config, 1, mpi_init_config, 0, MPI_COMM_WORLD);

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

        int number_amount;

        if(my_rank == LEADER){
            int i = 1;
            while(i<world_size){
                MPI_Status status;

                MPI_Probe(i, 0, MPI_COMM_WORLD, &status);

                // When probe returns, the status object has the size and other
                // attributes of the incoming message. Get the message size
                MPI_Get_count(&status, MPI_INT, &number_amount);


                // Allocate a buffer to hold the incoming numbers
                int* number_buf = (int*)malloc(sizeof(int) * number_amount);

                // Now receive the message with the allocated buffer
                MPI_Recv(number_buf, number_amount, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                printf("Leader dynamically received %d numbers from %d.\n", number_amount, i);
                free(number_buf);
                i++;
            }
        }else{
            const int MAX_NUMBERS = 100;
            int numbers[MAX_NUMBERS];
            // Pick a random amount of integers to send to process one
            number_amount = (rand() / (float)RAND_MAX) * MAX_NUMBERS;

            // Send the random amount of integers to process one
            MPI_Send(numbers, number_amount, MPI_INT, 0, 0, MPI_COMM_WORLD);
            printf("%d sent %d numbers to to leader\n", my_rank, number_amount);
        }

        current_time += init_config.t;
    }
    printf("POST: my rank: %d, position a: x=%f, y=%f\n", my_rank, non_infected_list->head->person->position.x, non_infected_list->head->person->position.y);
    printf("POST: my rank: %d, position b: x=%f, y=%f\n", my_rank, non_infected_list->head->next->person->position.x, non_infected_list->head->next->person->position.y);


    MPI_Type_free(&mpi_init_config);
    MPI_Finalize();
}