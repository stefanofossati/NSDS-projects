#include <mpi.h>

#include "mpi_datatypes.h"
#include "init_config.h"

MPI_Datatype create_mpi_init_config(){
    MPI_Datatype mpi_init_config;
    init_config_t config;

    /*
     * MPI_INIT (6 elements)
     */
    int num_blocks = 1;
    int block_lengths[] = {1, 1, 1, 1, 1, 1, 1,};

    MPI_Aint displacements[] = {
            (size_t) & (config.total_people) - (size_t) & config,
            (size_t) & (config.infected_people) - (size_t) & config,
            (size_t) & (config.W) - (size_t) & config,
            (size_t) & (config.L) - (size_t) & config,
            (size_t) & (config.w) - (size_t) & config,
            (size_t) & (config.l) - (size_t) & config,
    };

    MPI_Datatype block_types[] = {
            MPI_INT,
    };

    MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &config);
    MPI_Type_commit(&mpi_init_config);

    return mpi_init_config;
}
