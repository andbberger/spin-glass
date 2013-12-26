package spin-glass;

/** A lattice of possibly interacting spins.
 *  Lattices are not resizable. 
 *  @author Andrew Berger*/
abstract class Lattice {

    /** Returns the number of spins in this lattice.*/
    public abstract int latticeSize();

    /** Updates our lattice spin by spin until convergence is reached.*/
    public abstract void converge();

    /** Updates spin at IND, summing the activation function 
     *  over all predecessors of IND. */
    public abstract void updateSpin(int ind);

}