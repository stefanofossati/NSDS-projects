#ifndef PERSON_H
#define PERSON_H

typedef enum status {
    NON_INFECTED,
    INFECTED,
    IMMUNE
} status_t;

typedef struct position{
    float x;
    float y;
} position_t;


typedef struct person{
    position_t position;
    status_t status;
    int status_timer;
} person_t;

person_t *create_person( status_t status, float max_x, float max_y);

void update_person_position(person_t *person, float velocity, int delta_time, int theta, float max_x, float max_y);

#endif //PERSON_H