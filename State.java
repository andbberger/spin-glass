package spin_glass;

import java.util.BitSet;

/** Class holding a single state of arbitrary dimension 
 *  Internal representation is a bitset 
 *  @author Andrew Berger */
public class State {

    State(Spin[] lattice) {
        _size = lattice.length;
        _state = new BitSet();
        for (int i = 0; i < lattice.length; i++) {
            if (lattice[i].getBool() == 1) {
                _state.set(i);
            }
        }
    }

    State(BitSet state) {
        _state = state; 
    }


    public State[] generateBitFlips () { 
        State[] bitFlips = new State[size()];
        for (int i = 0; i < size(); i++) {
            _state.flip(i);
            bitFlips[i] = new State(_state);
            _state.flip(i);
        }
    }

    /** Returns my internal representation. */
    public BitSet getState() {
        return _state;
    }

    /** Returns the number of spins I hold.*/
    public int size() {
        return _state.size();
    }

    /** Returns the Ith spin*/
    public boolean getSpin(int i) {
        return _state.get(i);
    }

    
    /** Indexed by LSD */
    private BitSet _state;
}

