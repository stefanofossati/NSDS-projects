#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "person.h"
#include "init_config.h"

typedef struct node {
    person_t *person;
    struct node *next;
} node_t;

typedef struct linked_list {
    node_t *head;
    node_t *tail;
} linked_list_t;

linked_list_t *create_linked_list();

void linked_list_add(linked_list_t *list, person_t *person);

void linked_list_remove(linked_list_t *list, person_t *person);

void linked_list_free(linked_list_t *list);

int get_linked_list_length(linked_list_t *list);

//void linked_list_print(linked_list_t *list);
/**
 * DA SPOSTARE
 */

linked_list_t *create_people_linked_list(int num_people, status_t status, float max_x, float max_y);
void update_position_list(init_config_t *init_config, linked_list_t *people_list);

#endif //LINKEDLIST_H
