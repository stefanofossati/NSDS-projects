#include <mpi.h>

#include "mpi_datatypes.h"
#include "init_config.h"

MPI_Datatype create_mpi_init_config(){
    MPI_Datatype mpi_init_config;
    init_config_t init_config;

    /*
     * MPI_INIT (6 elements)
     */
    int num_blocks = 1;
    int block_lengths[] = {1, 1, 1, 1, 1, 1};

    MPI_Aint displacements[] = {
            (size_t) & (init_config.total_people) - (size_t) & init_config,
            (size_t) & (init_config.infected_people) - (size_t) & init_config,
            (size_t) & (init_config.W) - (size_t) & init_config,
            (size_t) & (init_config.L) - (size_t) & init_config,
            (size_t) & (init_config.w) - (size_t) & init_config,
            (size_t) & (init_config.l) - (size_t) & init_config,
    };

    MPI_Datatype block_types[] = {
            MPI_INT,
    };

    MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &init_config);
    MPI_Type_commit(&mpi_init_config);

    return mpi_init_config;
}
