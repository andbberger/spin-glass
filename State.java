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

    /** Returns true iff COMP differs from the state I represent
     *  By only one bitFlip.
     *  Throws an error if COMP is not of the same dimension as I.*/
    public boolean isBitFlipped(State comp) {
        if (comp.size() != _size) {
            throw new StateException("States are of different dimensions");
        }
        long[] cState = comp.getState();
        boolean findFlip = true;
        for (int ind = 0; ind < _state.length; ind++) {
            long masked = _state[ind] & cState[ind];
            if (findFlip && masked != 0) {
                if (isSingular(masked)) {
                    findFlip = false;
                } else {
                    return false;
                }
            } else if (!findFlip && masked != 0) {
                return false;
            }
        }
        return true;
    }

    /** Very important routine
     *  Naive implementation with java library call
     *  Deserving of some de bruijn magic*/
    private static int bitscanLSD(long state) {
        long lsb = state & -state;
        return Long.numberOfTrailingZeroes(lsb);
    }

    /** Returns true iff there is only one bit turned on in MASKED.*/
    private isSingular(long masked) {
        int on = bitscanLSD(masked);
        masked ^= (1 << on);
        return masked == 0;
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

