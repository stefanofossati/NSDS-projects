#include <stdio.h>
#include <stdlib.h>
//#include <mpi.h>

#include "utils/read_file.h"


int main(int argc, char **argv) {
    file_parameters_t  param = get_parameters_form_file("config.txt");

    printf("param = %d", param.N);

    return 0;
}