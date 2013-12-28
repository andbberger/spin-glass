package spin-glass;

/** A lattice of non-interacting spins.
 *  AKA a single layer in an RBM 
 *  @author Andrew Berger*/
public class NonInteractingLattice extends Lattice {

    NonInteractingLattice(Lattice predecessor) {
        _predecessor = predecessor;
        _weights = new Weighting(predecessor, this);
    }

    /** Fully deterministic spin updater.
     *  Always settles into a local min. */
    public void zeroTempSpinUpdate(int ind) {
        double sum = 0;
        for (int i = 0; i < predecessor.latticeSize(); i++) {
            sum += _weights[i][ind] * predecessor.getSpin(ind);
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
        /* This is what i was implementing here */
        /*http://en.wikipedia.org/wiki/Boltzmann_machine#Probability_of_a_unit.27s_state*/
    }
    
    private Lattice _predecessor;

}