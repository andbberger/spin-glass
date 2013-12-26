package spin-glass;


class Spin {
    
    Spin(boolean activated) {
        _threshold = 0;
        activation = activated ? 1 : -1;
    }
    
    /** 1 or -1 */
    private int activation;
    /** Threshold over which I activate.
     *  0 by default*/
    private int _threshold;
    
}