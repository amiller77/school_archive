mymake2 : mymake2.o parseInput.o graph.o
	gcc -Wall mymake2.o parseInput.o graph.o -o mymake2

mymake2.o : mymake2.c parseInput.c graph.c parseInput.h graph.h
	gcc -Wall -c mymake2.c

parseInput.o : parseInput.c parseInput.h graph.c graph.h
	gcc -Wall -c parseInput.c

graph.o : graph.c graph.h
	gcc -Wall -c graph.c

.PHONY: clean
clean:
	rm -f *.o mymake2