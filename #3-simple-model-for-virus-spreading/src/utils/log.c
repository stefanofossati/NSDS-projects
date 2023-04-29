#include <stdio.h>
#include "log.h"

void log_message(log_priority_t priority, char *message, int priority_level) {
    if(priority_level >= 3 && priority == INFO_3){
        printf("INFO_3: %s\n", message);
    }
    if(priority_level >= 2 && priority == INFO_2){
        printf("INFO_2: %s\n", message);
    }
    if(priority_level >= 1 && priority == INFO_1){
        printf("INFO_1: %s\n", message);
    }
    if(priority_level >= 0 && priority == WARNING){
        printf("WARNING: %s\n", message);
    }
    if(priority_level >= -1 && priority == ERROR){
        printf("ERROR: %s\n", message);
    }
}