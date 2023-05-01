#include <stdio.h>
#include <stdbool.h>

#include "log.h"
#include "country.h"
#ifndef FILE_READER_H
#define FILE_READER_H

typedef struct file_parameters{
    int N;
    int I;
    int W;
    int L;
    int w;
    int l;
    int v; //velocity
    int d; //distance infected
    int t; // time granularity
}file_parameters_t;

file_parameters_t get_parameters_from_file(char *file_name, int priority_level);

bool check_parameters(file_parameters_t *parameters);

void write_on_file(char *filename, country_number_t countries[], int total_length);

#endif //FILE_READER_H
