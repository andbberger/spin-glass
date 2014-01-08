package spin-glass;

/** The weighting between two possibly distinct Lattices
 *  @author Andrew Berger */
public class Weighting {

    /** A new weighting between two layers of an RBM.*/
    Weighting(Lattice from, Lattice to) {
        _from = from;
        _to = to;
        _weights = new double[from.latticeSize()][to.latticeSize()];
        _isInteracting = false;
    }

    /** A new weighting for a hopfield network.*/
    Weighting(InteractingLattice self) {
        _from = self;
        _to = self;
        _weights = new double[self.latticeSize()][self.latticeSize()];
        _isInteracting = true;
    }

    /** Set weight (I,J) to W.
     *  If isInteracting also sets (J, I) to W.*/
    public void set(int i, int j, int w) {
        if (i != j || !_isInteracting) {
            _weights[i][j] = w;
            if (_isInteracting) {
                _weights[j][i] = w;
            }
        } else {
            /* Self-interaction disallowed in hopfield networks*/
            _weights[i][j] == 0;
        }
    }

    /** Returns the weight at (I,J).*/
    public double get(int i, int j) {
        return _weights[i][j];
    }

    /** GRADIENT is the (wrapped) vector given by eq 16 in the MPF paper
     *  EPS the gradient step.
     *  GRADIENT of same dimensions of _weights
     *  Updates the weighting to a presumably better value. */
    public void gradientDescent(double[][] gradient, double eps) {
        for (int i = 0; i < _weights.length; i++) {
            for (int j = 0; j < _weights[0].length; j++) {
                _weights[i][j] -= eps * gradient[i][j]
            }
        }
    }

    /** Matrix holding weights.
     *  Square and symmetric and (i, i) = 0 if isInteracting. */
    private double[][] _weights;
    /** True iff I hold weights for a hopfield network. */
    private boolean _isInteracting;
    private Lattice _from;
    private Lattice _to;

}