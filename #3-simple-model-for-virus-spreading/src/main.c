#include <stdio.h>
#include <stdlib.h>
//#include <mpi.h>

#include "utils/read_file.h"


int main(int argc, char **argv) {
    file_parameters_t  param = get_parameters_form_file("config.txt");

    if(!check_parameters(&param)){
        printf("Error in parameters");
        // MPI ABORT
        return 1;
    }else {
        printf("Parameters are correct");
    }

    printf("parameters are: N = %d, I = %d, W = %d, L = %d, w = %d, l = %d, v = %d, d = %d, t = %d\n", param.N, param.I, param.W, param.L, param.w, param.l, param.v, param.d, param.t);

    return 0;
}