#ifndef COUNTRY_H
#include "person.h"
#define COUNTRY_H

typedef struct country_number{
    int non_infected_person;
    int infected_person;
    int immune_person;
} country_number_t;


void person_in_country(person_t *person, int W, int L, int w, int l, country_number_t countries[][L/l]);

country_number_t *convert_to_array(int width, int length, country_number_t *countries_array, country_number_t countries[][length]);

void merge_country_arrays(country_number_t countries_array[], country_number_t received_array[], int length);

#endif //COUNTRY_H