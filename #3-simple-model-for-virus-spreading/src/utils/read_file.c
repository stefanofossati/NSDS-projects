#include <stdlib.h>
#include <stdbool.h>
#include <mpi.h>

#include "read_file.h"
#include "log.h"

/**
 * This function reads form the config file and returns a struct with all the parameters
 * @param file_name
 * @return
 */
file_parameters_t get_parameters_from_file(char *file_name, int priority_log){
    FILE *file = fopen(file_name, "r");

    if(file == NULL){
        log_message(ERROR, "Error in opening file", priority_log);
        //MPI ABORT
    }

    file_parameters_t *parameters = malloc(sizeof(file_parameters_t));

    fscanf(file, "N = %d\n", &parameters->N);
    fscanf(file, "I = %d\n", &parameters->I);
    fscanf(file, "W = %d, L = %d\n", &parameters->W, &parameters->L);
    fscanf(file, "w = %d, l = %d\n", &parameters->w, &parameters->l);
    fscanf(file, "v = %f\n", &parameters->v);
    fscanf(file, "d = %d\n", &parameters->d);
    fscanf(file, "t = %d\n", &parameters->t);

    fclose(file);

    return *parameters;
}

bool check_parameters(file_parameters_t *parameters){
    // The parameters must be greater than 0
    if(parameters->N <= 0){
        printf("N must be greater than 0");
        return false;
    }
    if(parameters->I <= 0){
        printf("I must be greater than 0");
        return false;
    }
    if(parameters->W <= 0 || parameters->L <= 0){
        printf("W and L must be greater than 0");
        return false;
    }
    if(parameters->w <= 0 || parameters->l <= 0){
        printf("w and l must be greater than 0");
        return false;
    }
    if(parameters->v <= 0.0){
        printf("v must be greater than 0");
        return false;
    }
    if(parameters->d <= 0){
        printf("d must be greater than 0");
        return false;
    }
    if(parameters->t <= 0){
        printf("t must be greater than 0");
        return false;
    }

    // The parameters W and w, L and l are correlated
    if(parameters->W < parameters->w){
        printf("W must be greater than w");
        return false;
    }
    if(parameters->L < parameters->l){
        printf("L must be greater than l");
        return false;
    }

    //the parameters must be divisible
    if(parameters->W % parameters->w != 0){
        printf("W must be divisible by w");
        return false;
    }
    if(parameters->L % parameters->l != 0){
        printf("L must be divisible by l");
        return false;
    }

    return true;
}

void write_on_file(FILE *file, country_number_t countries[], int total_length, int simulation_day){
    for(int i=0; i<total_length; i++){
        fprintf(file, "%d,%d,%d,%d,%d\n",simulation_day, i, countries[i].non_infected_person, countries[i].infected_person, countries[i].immune_person);
    }
}