#ifndef LOG_H
#define LOG_H

typedef enum log_priority {
    INFO_1,
    INFO_2,
    INFO_3,
    WARNING,
    ERROR
} log_priority_t;

void log_message(log_priority_t priority, char *message, int priority_level);

#endif //LOG_H
