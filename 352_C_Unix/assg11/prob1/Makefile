bacon : bacon.o graph.o
	gcc -Wall bacon.o graph.o -o bacon

bacon.o : bacon.c graph.h
	gcc -Wall -c bacon.c

graph.o : graph.c graph.h
	gcc -Wall -c graph.c


.PHONY: clean
clean:
	rm -f *.o bacon