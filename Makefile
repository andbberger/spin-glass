SHELL = bash

PACKAGE = spin_glass

DESTDIR = bin

JFLAGS = -g -Xlint:unchecked 

SRCS = $(wildcard $(PACKAGE)/*.java)

CLASSES = $(SRCS:.java=.class)

# Tell make that these are not really files.
.PHONY: clean default compile style  \
	check unit blackbox jar dist

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: compile

compile: $(CLASSES)

$(CLASSES): $(PACKAGE)/sentinel

$(PACKAGE)/sentinel: $(SRCS)
	javac $(JFLAGS) $(SRCS)
	touch $@

jar:
	$(RM) -r classes
	mkdir classes
	javac -d classes $(SRCS)
	cp $(RESOURCES) classes/$(PACKAGE)
	cd classes; jar xf $(UCBJAR) ucb/util; \
	jar cvfe ../bin/$(PACKAGE).jar $(PACKAGE).Main $(PACKAGE) ucb

# Find and remove all *~ and *.class files, and the generated jar
# files.  Do not touch .git directories.
clean:
	$(RM) */sentinel bin/*.jar
	$(RM) -r classes
	find . -name .git -prune -o \
            \( -name '*.out' -o -name '*.class' -o -name '*~' \) \
            -exec $(RM) {} \;
