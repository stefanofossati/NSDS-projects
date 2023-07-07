#include "init_config.h"

init_config_t set_init_config(int total_people, int infected_people, int W, int L, int w, int l, float v, int d, int t){
    init_config_t init_config;
    init_config.total_people = total_people;
    init_config.infected_people = infected_people;
    init_config.W = W;
    init_config.L = L;
    init_config.w = w;
    init_config.l = l;
    init_config.d = d;
    init_config.v = v;
    init_config.t = t;
    return init_config;
}