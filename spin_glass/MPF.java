package spin_glass;

import java.util.List;
import static spin_glass.Constants.*;

/* To Do: 
   -write energy gradients in Lattice
   -write gradient descent 
   -not sure explicit sparse matrix is necessary 
   -really need static class to compute energy given state and weight
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

    public void fit() {
        double[][] gradient = KLGradient();
        //or maybe while greater than some epsillon?
        while (magnitude(gradient) != 0) {
            _spinGlass.updateWeights(gradient);
            gradient = KLGradient();
        }
    }

    public Lattice getLattice() {
        return _spinGlass;
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
                gradient[i][j] = KLPartial(i, j);           
            }
        }
        return gradient;
    }

    /** Returns the L2norm of the gradient vector.*/
    private double magnitude(double[][] gradient) {
        double mag = 0;
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                mag += Math.pow(gradient[i][j], 2);
            }
        }
        return mag;
    }

    /** Computes the numerical derivative of MPF objective
     *  func wrt weight n m. */
    private double KLPartial(int n, int m) {
        double partial = 0;
        for (State obs: _observations) {
            State[] bitFlips = obs.generateBitFlips();
            for (int i = 0; i < _spinGlass.latticeSize(); i++) {
                partial += _spinGlass.bitFlippedPartial(n, m, i) 
                    * computeEntry(obs, i);
            }
        }
        return partial * (EPSILLON / _observations.size());        
    }

    

    /** Returns the computed weighted value of an entry in gamma.
     *  Namely the difference in energy from flipping bit N of STATE,
     *  weighted as prescribed in the paper. */
    private double computeEntry(State s, int n) {
        _spinGlass.setSpins(s);
        double energy = _spinGlass.getEnergy();
        int currSpin = _spinGlass.getSpin(n);
        double flipE = _spinGlass.energyDiff(n);
        double deltaE;
        //SIGNS!!
        if (currSpin == 1) {
            deltaE = energy + flipE;
        } else {
            deltaE = energy - flipE;
        }
        deltaE = deltaE / 2;
        return Math.pow(Math.E, deltaE);
    }
     
    /** The lattice we are training.  
     *  Spins are frequently changed to compute energies, weights are
     *  updated through gradient descent of MPF objective function. */
    Lattice _spinGlass;
    List<State> _observations;
    /** The transition probability matrix
     *  Entry ij is of the form g ij exp (1/2(E j (R) - E i (R)))
     *  R is the weight vector and g ij is 1 iff states ij are connected
     *  Only j in obeservations are present */
    SparseMatrix<Double> _gamma; 
}