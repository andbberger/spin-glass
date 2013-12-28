package spin-glass;

/** Object with fields and methods for a spin.
 *  @author Andrew Berger*/
class Spin {
    
    /** A new spin.*/
    Spin(boolean activated) {
        _threshold = 0;
        _activation = activated ? 1 : -1;
    }

    /** Sets my value to 1 iff ACTIVATED.*/
    public void set(boolean activated) {
        _activation = activated ? 1 : -1;
    }

    /** Returns 1 if activated, otherwise -1.*/
    public int get() {
        return _activation;
    }

    /** Returns the value of my threshold. */
    public int getThreshold() {
        return _threshold;
    }
    
    /** 1 or -1 */
    private int _activation;
    /** Threshold over which I activate.
     *  0 by default*/
    private int _threshold;
    
}