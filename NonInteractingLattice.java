package spin-glass;

/** A lattice of non-interacting spins.
 *  AKA a single layer in an RBM 
 *  @author Andrew Berger*/
public class NonInteractingLattice extends Lattice {

    /** A new non-interacting lattice with NUMSPINS.
     *  Initially all spins set to -1. */
    NonInteractingLattice(int numSpins, Lattice predecessor) {
        _predecessor = predecessor;
        _weights = new Weighting(predecessor, this);
        _size = numSpins;
        _lattice = new Spin[numSpins];
        for (int i = 0; i < numSpins; i++) {
            _lattice[i] = new Spin(false);
        }
        _rand = new Random();
    }

    @Override 
    public void converge() {
        for (int i = 0; i < latticeSize(); i++) {
            updateSpin(ind);
        }
    }

    @Override
    public Lattice predecessor() {
        return _predecessor;
    }

    
    private Lattice _predecessor;
}