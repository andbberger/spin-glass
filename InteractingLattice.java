package spin_glass;

import java.util.BitSet;

/** A lattice of interacting spins.
 *  AKA a hopfield network
 *  @author Andrew Berger */
public class InteractingLattice extends Lattice {

    /** A new interacting lattice with NUMSPINS.
     *  Initially all spins set to -1.*/
    InteractingLattice(int numSpins) {
        _weights = new Weighting(this);
        _size = numSpins;
        _lattice = new Spin[numSpins];
        for (int i = 0; i < numSpins; i++) {
            _lattice[i] = new Spin(false);
        }
        _rand = new Random();
        _updateSpins = new BitSet();
    }
    
    @Override 
    public void converge() {
        if (_setTemperature == 0) {
            descendGradient();
        } else {
            anneal();
        }
    }

    /** Sets the spins in this glass to their corresponding
     *  values in state. */
    public void setSpins(State state) {
        for (int i = 0; i < state.size(); i++) {
            _lattice[i].set(state.getSpin(i));
        }
    }

    /** Gradually lowers temperature while updating spins randomly.
     *  Calls descendGradient when zero temperature is reached. 
     *  Annealing rule is currently a simple constant decrement per update. 
     *  Much more sophisticated annealing rules possible.*/
    private void anneal() {
        while (_temperature > 0) {
            boltzmannSpinUpdate(randSpin());
            _temperature -= ANNEAL_DECR;
        }
        _temperature = _setTemperature;
        descendGradient();
    }

    /** Deterministically updates a random spin.
     *  Keeps track of already updated spins
     *  Halts when we are guaranteed to have reached a fixed point */
    private void descendGradient() {
        long repr = representation();
        int spin = randSpin();
        do {
            zeroTempSpinUpdate(spin);
            if (representation() == repr) {
                _updatedSpins.set(spin);
            } else {
                _updatedSpins.clear();
                repr = representation();
            }
            spin = randSpin();
        } while (spin != -1);
        _updatedSpins.clear();
    }

    /** Returns an index to a random spin that hasn't been updated.
     *  Returns -1 if there is no spin that hasn't been updated.
     *  This solution is somewhat naive, but should be fast enough. */
    private int randSpin() {
        int start = _rand.nextInt(latticeSize());
        int closest = _updatedSpins.nextClearBit(start);
        if (closest == -1) {
            closest = _updatedSpins.previousClearBit(start);
        }
        return closest; 
    }

    @Override
    public Lattice predecessor() {
        return this;
    }
    
    /** Keeps track of spins we have already updated.*/
    private BitSet _updatedSpins;
    /** Amount by which temperature is decremented at each update step*/
    private static final int ANNEAL_DECR == .1


}