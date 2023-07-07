#include <mpi.h>

#include "mpi_datatypes.h"
#include "init_config.h"
#include "person.h"
#include "country.h"

MPI_Datatype create_mpi_init_config(){
    MPI_Datatype mpi_init_config;
    init_config_t init_config;

    /*
     * MPI_INIT (9 elements)
     */
    int num_blocks = 9;
    int block_lengths[] = {1, 1, 1, 1, 1, 1, 1, 1, 1};

    MPI_Aint displacements[] = {
            (size_t) & (init_config.total_people) - (size_t) & init_config,
            (size_t) & (init_config.infected_people) - (size_t) & init_config,
            (size_t) & (init_config.W) - (size_t) & init_config,
            (size_t) & (init_config.L) - (size_t) & init_config,
            (size_t) & (init_config.w) - (size_t) & init_config,
            (size_t) & (init_config.l) - (size_t) & init_config,
            (size_t) & (init_config.v) - (size_t) & init_config,
            (size_t) & (init_config.d) - (size_t) & init_config,
            (size_t) & (init_config.t) - (size_t) & init_config,
    };

    MPI_Datatype block_types[] = {
            MPI_INT,
            MPI_INT,
            MPI_INT,
            MPI_INT,
            MPI_INT,
            MPI_INT,
            MPI_FLOAT,
            MPI_INT,
            MPI_INT
    };

    MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &mpi_init_config);
    MPI_Type_commit(&mpi_init_config);

    return mpi_init_config;
}


MPI_Datatype create_mpi_position(){
        MPI_Datatype mpi_position;
        position_t position;

        int num_blocks = 2;
        int block_lengths[] = {1, 1};

        MPI_Aint displacements[] = {
                (size_t) & (position.x) - (size_t) & position,
                (size_t) & (position.y) - (size_t) & position,
        };

        MPI_Datatype block_types[] = {
                MPI_FLOAT,
                MPI_FLOAT,
        };

        MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &mpi_position);
        MPI_Type_commit(&mpi_position);

        return mpi_position;
}

MPI_Datatype create_mpi_person(MPI_Datatype MPI_POSITION){
        MPI_Datatype mpi_person;
        person_t person;

        int num_blocks = 3;
        int block_lengths[] = {1, 1, 1};

        MPI_Aint displacements[] = {
                (size_t) & (person.position) - (size_t) & person,
                (size_t) & (person.status) - (size_t) & person,
                (size_t) & (person.status_timer) - (size_t) & person,
        };

        MPI_Datatype block_types[] = {
                MPI_POSITION,
                MPI_INT,
                MPI_INT
        };

        MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &mpi_person);
        MPI_Type_commit(&mpi_person);

        return mpi_person;
}

MPI_Datatype create_mpi_country_number(){
        MPI_Datatype mpi_country_number;
        country_number_t country_number;

        int num_blocks = 3;
        int block_lengths[] = {1, 1, 1};

        MPI_Aint displacements[] = {
                (size_t) & (country_number.non_infected_person) - (size_t) & country_number,
                (size_t) & (country_number.infected_person) - (size_t) & country_number,
                (size_t) & (country_number.immune_person) - (size_t) & country_number,
        };

        MPI_Datatype block_types[] = {
                MPI_INT,
                MPI_INT,
                MPI_INT
        };

        MPI_Type_create_struct(num_blocks, block_lengths, displacements, block_types, &mpi_country_number);
        MPI_Type_commit(&mpi_country_number);

        return mpi_country_number;
}
