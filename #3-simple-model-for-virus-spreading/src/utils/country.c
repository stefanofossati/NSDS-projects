#include "person.h"

int person_in_country(person_t *person, int W, int L, int w, int l) {
    int country_y;
    int country_x;

    for(int i,j = 0; i<W; i=i+w, j++){
        if(person->position.x >= i && person->position.x < i+w){
            for(int k, h = 0; h<L; h=h+l, k++) {
                if (person->position.y >= h && person->position.y < h + l) {
                    country_x = h;
                    country_y = k;
                    break;
                }
            }
        }
    }

    return country_x + country_y; // this is wrong

}
