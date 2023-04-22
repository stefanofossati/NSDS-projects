#include "linked_list.h"
#include <stdlib.h>

/**
 * Initialization of a linked list
 * @param list
 */
void linked_list_init(linked_list_t *list) {
    list->head = NULL;
    list->tail = NULL;
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

