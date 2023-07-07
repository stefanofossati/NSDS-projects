CC = mpicc
CFLAGS = -Wall #-Wall = shows the error and warning messages
LDLIBS = -lm #math library
OUTPUT_DIR = output
.SUFFIXES: exec

exec = main
objects = main.o read_file.o person.o mpi_datatypes.o init_config.o linked_list.o log.o country.o


$(exec): $(objects)
	cd $(OUTPUT_DIR) && $(CC) $(LDFLAGS) $^ $(LOADLIBES) $(LDLIBS) -o $@ #it's a pattern rule for all the objects

log.o: utils/log.c utils/log.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

init_config.o: utils/init_config.c utils/init_config.h utils/log.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

read_file.o: utils/read_file.c utils/read_file.h utils/log.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

mpi_datatypes.o: utils/mpi_datatypes.c utils/mpi_datatypes.h utils/init_config.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

person.o: utils/person.c utils/person.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

linked_list.o: utils/linked_list.c utils/linked_list.h utils/person.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

country.o: utils/country.c utils/country.h utils/person.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

main.o: main.c utils/read_file.h  utils/mpi_datatypes.h utils/init_config.h utils/linked_list.h utils/log.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $(OUTPUT_DIR)/$@

build: $(exec)

all:
	mkdir -p $(OUTPUT_DIR)

run:
	cd $(OUTPUT_DIR) && mpirun -np 8 ./main

clean:
	rm -rf $(OUTPUT_DIR)