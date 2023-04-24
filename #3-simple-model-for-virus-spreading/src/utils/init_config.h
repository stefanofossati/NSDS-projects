#ifndef INIT_CONFIG_H
#define INIT_CONFIG_H
typedef struct init_config{
    int total_people;
    int infected_people;
    int W;
    int L;
    int w;
    int l;
} init_config_t;

init_config_t set_init_config(int total_people, int infected_people, int W, int L, int w, int l);

#endif //INIT_CONFIG_H
