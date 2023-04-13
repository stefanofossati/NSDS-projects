#include <stdio.h>
#include <stdlib.h>
//#include <mpi.h>

#include "utils/read_file.h"


void main() {
    file_parameters_t  param = get_parameters_form_file("config.txt");

    printf("param = %d", param.N);

}