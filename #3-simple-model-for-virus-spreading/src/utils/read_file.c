#include <stdlib.h>

#include "read_file.h"


file_parameters_t get_parameters_form_file(char *file_name){
    FILE *file = fopen(file_name, "r");

    if(file == NULL){
        printf("Error in opening file");
        //MPI ABORT
    }

    file_parameters_t *parameters = malloc(sizeof(file_parameters_t));

    fscanf(file, "N = %d\n", &parameters->N);
    fscanf(file, "I = %d\n", &parameters->I);
    fscanf(file, "W = %d, L = %d\n", &parameters->W, &parameters->L);
    fscanf(file, "w = %d, l = %d\n", &parameters->w, &parameters->l);
    fscanf(file, "v = %d\n", &parameters->v);
    fscanf(file, "d = %d\n", &parameters->d);
    fscanf(file, "t = %d\n", &parameters->t);

    return *parameters;
}