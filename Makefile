#Very simple no frills makefile 
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        State.java \
        Lattice.java \
        InteractingLattice.java \
        NonInteractingLattice.java \
	Spin.java \
	Sampling.java \
	SparseMatrix.java \
	MPF.java \
	Weighting.java \
	Constants.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class