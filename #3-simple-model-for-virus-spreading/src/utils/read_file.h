#include <stdio.h>
#include <stdbool.h>
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

file_parameters_t get_parameters_form_file(char *file_name);
bool check_parameters(file_parameters_t *parameters);
#endif //FILE_READER_H
