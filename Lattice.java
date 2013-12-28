package spin-glass;

import java.util.Random;

/** A lattice of possibly interacting spins.
 *  Lattices are not resizable. 
 *  @author Andrew Berger*/
abstract class Lattice {

    /** Updates our lattice spin by spin until convergence is reached.*/
    public abstract void converge();

    /** Returns the lattice that interacts with the lattice I implement. */
    public abstract Lattice predecessor();

    /** Returns the number of spins in this lattice.*/
    public int latticeSize() {
        return _size;
    }

    /** Updates spin at IND, summing the activation function 
     *  over all predecessors of IND. */
    public void updateSpin(int ind) {
        if (_temperature == 0) {
            zeroTempSpinUpdate(ind);
        } else {
            boltzmannSpinUpdate(ind);
        }
    }

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

    /** Fully deterministic spin updater.
     *  Always settles into a local min. */
    private void zeroTempSpinUpdate(int ind) {
        double sum = 0;
        for (int i = 0; i < predecessor().latticeSize(); i++) {
            sum += _weights[i][ind] * predecessor().getSpin(ind);
        }
        if (sum > getThreshold(ind)) {
            setSpin(ind, true);
        } else {
            setSpin(ind, false);
        }
    }

    /** Result of E(spin at ind = -1) - E(spin at ind = 1) */
    private double energyDiff(int ind) {
        double e = 0;
        for (int i = 0; i < predecessor().latticeSize(); i++) {
            e += _weights[i][ind] * predecessor().getSpin(ind);
        }
        // check the sign here
        e -= getThreshold(ind);
        return e;
    }

    /** Probabilistic spin updater.
     *  Has a probability to settle into a higher energy state,
     *  which is proportional to the temperature of the system
     *  In this way can 'tunnel' out of a local min. */
    private void boltzmannSpinUpdate(int ind) {
        double boltzmannFactor = Math.pow(Math.E, -(energyDiff(ind) / _temperature));
        double probOn = 1 / (1 + boltzmannFactor);
        if (_rand.nextDouble() <= probOn) {
            setSpin(ind, true);
        } else {
            setSpin(ind, false);
        }
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
    /** The PRNG. */
    private Random _rand; 

}