package spin-glass;

import java.util.List;

/* To Do: 
   -write energy gradients in Lattice
   -write gradient descent 
*/

/** Class implementing Minimum Probability Flow learning
 *  Initial implementation will only support Ising model,
 *  RBM will be supported as I better understand MPF
 *  @author Andrew Berger*/
public class MPF {

    MPF(List<State> observations, Lattice spinGlass) {
        _observations = observations;
        _spinGlass = spinGlass;
    }

    /** Iterates through the obersations, filling gamma with an
     *  observation and it's bit flipped pairs.*/
    private void populateGamma() {
        for (State obs : _observations) {
            State[] bitFlips = obs.generateBitFlips();
            for (int n = 0; n < bitFlips.length; n++) {
                _gamma.put(bitFlips[n], obs, 
                           computeEntry(bitFlips[n], n)); 
            }
        }
    }
   
    /** Fills a matrix of the same dimension of the weight matrix
     *  With the numerical derivative of that weight of 
     *  the KL divergence. */
    private double[][] KLGradient() {
        int iSize = _spinGlass.predecessor().latticeSize();
        int jSize = _spinGlass.latticeSize();
        double[][] gradient = new double[iSize][jSize];
        for (int i = 0; i < iSize; i++) {
            for (int j = 0; j < jSize; j++) {
                //computing eq 16 
                //the derivative I wrote earlier wasn't entirely correct.
                //Would require having lattice object for every bit flip
            }
        }
    }

    /** Returns the computed weighted value of an entry in gamma.
     *  Namely the difference in energy from flipping bit N of STATE,
     *  weighted as prescribed in the paper. */
    private double computeEntry(Lattice state, int n) {
        double energy = state.getEnergy();
        int currSpin = state.getSpin(n);
        double flipE = state.energyDiff(n);
        double deltaE;
        //SIGNS!!
        if (currSpin == 1) {
            deltaE = currEnergy + flipE;
        } else {
            deltaE = currEnergy - flipE;
        }
        deltaE = deltaE / 2;
        return Math.pow(Math.E, deltaE);
    }
     
    /** The lattice we are training.  */
    Lattice _spinGlass;
    List<State> _observations;
    /** The transition probability matrix
     *  Entry ij is of the form g ij exp (1/2(E j (R) - E i (R)))
     *  R is the weight vector and g ij is 1 iff states ij are connected
     *  Only j in obeservations are present */
    SparseMatrix<double> _gamma; 
}