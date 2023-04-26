#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

#include "utils/read_file.h"
#include "utils/init_config.h"
#include "utils/mpi_datatypes.h"

#define LEADER 0

int main(int argc, char **argv) {
    printf("start initializaion\n");
    file_parameters_t  param = get_parameters_form_file("../config.txt");

    MPI_Init(NULL, NULL);

    int my_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Create MPI_Datatypes
    MPI_Datatype mpi_init_config = create_mpi_init_config();

    init_config_t init_config;
    if(my_rank == LEADER){
        if(!check_parameters(&param)){
            printf("Error in parameters");
            MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            return 1;
        }else {
            printf("Parameters are correct\n");
        }
        printf("parameters are: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d, v = %d, d = %d, t = %d\n", param.N, param.I, param.W, param.L, param.w, param.l, param.v, param.d, param.t);
        //Maybe check in the number of individuals is divisible by the number of processes
        init_config = set_init_config((int)param.N/world_size, (int)param.I/world_size, param.W, param.L, param.w, param.l);
    }

    /* Broadcast the configuration to all processes */
    MPI_Bcast(&init_config, 1, mpi_init_config, 0, MPI_COMM_WORLD);

    printf("my rank % d, init_config: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d\n", my_rank, init_config.total_people, init_config.infected_people, init_config.W, init_config.L, init_config.w, init_config.l);

    MPI_Type_free(&mpi_init_config);
    MPI_Finalize();
}