#include "person.h"

#ifndef LINKEDLIST_H
#define LINKEDLIST_H

typedef struct node {
    person_t *person;
    struct node *next;
} node_t;

typedef struct linked_list {
    node_t *head;
    node_t *tail;
} linked_list_t;

void linked_list_init(linked_list_t *list);

void linked_list_add(linked_list_t *list, person_t *person);

void linked_list_remove(linked_list_t *list, person_t *person);

void linked_list_free(linked_list_t *list);

void linked_list_print(linked_list_t *list);

#endif //LINKEDLIST_H
