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

    /** Returns the current activation of the spin at IND.*/
    public int getSpin(int ind) {
        return _lattice[ind].get();
    }

    /** Returns the threshold of spin at IND. */
    public int getThreshold(int ind) {
        return _lattice[ind].getThreshold();
    }

    /** Sets the spin at IND to 1 iff ACTIVATED is true. */
    public void setSpin(int ind, boolean activated) {
        _lattice[ind].set(activated);
    }

    /** Our lattice is in equilibrium with a heat bath at TEMPERATURE.
     *  Always >= 0.
     *  Setting temperature to 0 yields a fully deterministic lattice.
     *  For temp > 0, the lattice has a probability to 'tunnel' out 
     *  of a local min*/
    private double _temperature;
    /** The spins in this lattice.*/
    private Spin[] _lattice;
    /** The weights between spins in this lattice.*/
    private Weighting _weights;
    /** Number of spins in this lattice. */
    private int _size;

}