#include <mpi.h>

#ifndef MPI_DATATYPES_H
#define MPI_DATATYPES_H

MPI_Datatype create_mpi_init_config();

MPI_Datatype create_mpi_position();

MPI_Datatype create_mpi_person(MPI_Datatype MPI_POSITION);

MPI_Datatype create_mpi_country_number();

#endif //MPI_DATATYPES_H
