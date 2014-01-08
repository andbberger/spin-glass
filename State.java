package spin-glass;

/** Class holding a single state of arbitrary dimension 
 *  Internal representation is an array of longs 
 *  @author Andrew Berger */
public class State {

    State(Spin[] lattice) {
        _size = lattice.length;
        //Proper rounding behavior???
        int reqSize = lattice.length / 64;
        _state = new long[reqSize];
        for (int i = 0; i < reqSize; i++) {
            for (int j = 0; j < 64; j++) {
                _state[i] |= (lattice[i * 64 + j].getBool() << j); 
            }
        }
    }

    State(long[] state) {
        _state = state;
    }


    public State[] generateBitFlips () { 
        State[] bitFlips = new State[_size];
        for (int ind = 0; ind < _state.length; ind++) {
            for (int b = 0; b < 64; b++) {
                _state[ind] ^= (1 << b);
                bitFlips[ind*64 + b] = State(_state);
                _state[ind] ^= (1 << b);
            }
        }
    }

    /** Returns my internal representation. */
    public long[] getState() {
        return _state;
    }

    /** Returns the number of spins I hold.*/
    public double size() {
        _size;
    }
    
    /** Indexed by LSD */
    private long[] _state;
    private double _size;
}

