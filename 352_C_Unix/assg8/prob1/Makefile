mymake : mymake.o parseInput.o graph.o
	gcc -Wall mymake.o parseInput.o graph.o -o mymake

mymake.o : mymake.c parseInput.c graph.c parseInput.h graph.h
	gcc -Wall -c mymake.c

parseInput.o : parseInput.c parseInput.h graph.c graph.h
	gcc -Wall -c parseInput.c

graph.o : graph.c graph.h
	gcc -Wall -c graph.c

.PHONY: clean
clean:
	rm -f *.o mymake