#include "person.h"

void person_in_country(person_t *person, int W, int L, int w, int l, country_number_t** countries) {
    int country_y;
    int country_x;

    for(int i,j = 0; i<W; i=i+w, j++){
        if(person->position.x >= i && person->position.x < i+w){
            for(int k, h = 0; h<L; h=h+l, k++) {
                if (person->position.y >= h && person->position.y < h + l) {
                    country_x = h;
                    country_y = k;
                    switch (person->status) {
                        case NON_INFECTED:
                            countries[country_x][country_y].non_infected_person++;
                            break;
                        case INFECTED:
                            countries[country_x][country_y].infected_person++;
                            break;
                        case IMMUNE:
                            countries[country_x][country_y].immune_person++;
                            break;
                    }
                    return;
                }
            }
        }
    }
}

country_number_t *convert_to_array(country_number_t** countries, int width, int length){
    country_number_t countries_array[width*length];
    //[1][2][3][4]
    //[5][6][7][8]
    //[9][10][11][12]

    //TODO check if this is correct
    for(int i=0; i<length; i++){
        for(int j=0; j<width; j++){
            countries_array[i*length+j] = countries[j][i];
        }
    }

    return countries_array;
}

void merge_country_arrays(country_number_t countries_array[], country_number_t received_array[], int length){
    for(int i=0; i<length; i++){
        countries_array[i].non_infected_person += received_array[i].non_infected_person;
        countries_array[i].infected_person += received_array[i].infected_person;
        countries_array[i].immune_person += received_array[i].immune_person;
    }
}
