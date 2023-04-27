#include <stdlib.h>
#include <math.h>

#include "person.h"

/**
 * This function initializes the person
 * @param status
 * @param max_x
 * @param max_y
 * @return
 */
person_t *create_person(status_t status, float max_x, float max_y) {
    person_t *person = malloc(sizeof(person_t));
    person->status = status;
    switch (status) {
        case NON_INFECTED:
            person->status_timer = 10*60; // 10 minutes
            break;
        case INFECTED:
            person->status_timer = 10*24*60*60; // 10 days
            break;
        case IMMUNE:
            person->status_timer = 28*3*24*60*60; // 28 days represent 1 month
            break;

    }
    person->position.x = -max_x/2 + ((float)rand()) /((float)((RAND_MAX/max_x)));
    person->position.y = -max_y/2 + ((float)rand()) /((float)((RAND_MAX/max_y)));
    return person;
}

/**
 * this function updates the position of the person taking in consideration the limited area
 * @param person
 * @param velocity
 * @param delta_time
 * @param theta
 * @param max_x
 * @param max_y
 */
void update_person_position(person_t *person, float velocity, int delta_time, int theta, float max_x, float max_y) {
    float new_pos_x = person->position.x + velocity * delta_time * cos(theta);
    float new_pos_y = person->position.y + velocity * delta_time * sin(theta);

    // these ifs check if the person is passing over the limited area
    if(new_pos_x<-max_x/2){
        new_pos_x += max_x;
    }else if(new_pos_x>max_x/2){
        new_pos_x -= max_x;
    }

    if(new_pos_y<-max_y/2) {
        new_pos_y += max_y;
    }else if(new_pos_y>max_y/2){
        new_pos_y -= max_y;
    }

    person->position.x = new_pos_x;
    person->position.y = new_pos_y;
}