#include "linked_list.h"
#include "init_config.h"
#include <stdlib.h>

/**
 * Creates a linked list
 * @param list
 * @return
 */
linked_list_t *create_linked_list() {
    linked_list_t *list = malloc(sizeof(linked_list_t));
    list->head = NULL;
    list->tail = NULL;

    return list;
}

/**
 * Add a element to the end of a linked list
 * @param list
 * @param person
 */
void linked_list_add(linked_list_t *list, person_t *person) {
    node_t *node = malloc(sizeof(node_t));
    node->person = person;
    node->next = NULL;

    if (list->head == NULL) {
        list->head = node;
        list->tail = node;
    } else {
        list->tail->next = node;
        list->tail = node;
    }
}

/**
 * Removes an element form a linked_list
 * @param list
 * @param person
 */
void linked_list_remove(linked_list_t *list, person_t *person) {
    node_t *current = list->head;
    node_t *previous = NULL;

    while (current != NULL) {
        if (current->person == person) {
            if (previous == NULL) {
                list->head = current->next;
            } else {
                previous->next = current->next;
            }

            if (current->next == NULL) {
                list->tail = previous;
            }

            free(current);
            return;
        }

        previous = current;
        current = current->next;
    }
}

/**
 * Deletes a linked_list
 * @param list
 */
void linked_list_free(linked_list_t *list) {
    node_t *current = list->head;
    node_t *next = NULL;

    while (current != NULL) {
        next = current->next;
        free(current);
        current = next;
    }

    list->head = NULL;
    list->tail = NULL;
}

int get_linked_list_length(linked_list_t *list) {
    node_t *current = list->head;
    int length = 0;

    while (current != NULL) {
        length++;
        current = current->next;
    }

    return length;
}

/**
 * DA SPOSTARE
 */

linked_list_t *create_people_linked_list(int num_people, status_t status, float max_x, float max_y){
    linked_list_t *people_list = create_linked_list();

    for(int i=0; i<num_people; i++){
        person_t *person = create_person(status, max_x, max_y);
        linked_list_add(people_list, person);
    }

    return people_list;
}

person_t *linked_list_to_array(linked_list_t *list){
    int length = get_linked_list_length(list);
    person_t *people_array = malloc(sizeof(person_t) * length);

    node_t *current = list->head;
    int i = 0;

    while (current != NULL) {
        people_array[i] = *current->person;
        current = current->next;
        i++;
    }

    return people_array;
}

void update_position_list(init_config_t *init_config, linked_list_t *people_list){
    node_t *current = people_list->head;
    int theta;

    while (current != NULL) {
        theta = rand() % 360;
        update_person_position(current->person, init_config->v, init_config->t, theta, init_config->L, init_config->W);
        current = current->next;
    }
}

