
%.png: %.dot
	dot -Tpng -o $*.png $<


DOT_FILES=$(wildcard *.dot)

all: $(DOT_FILES:.dot=.png)

