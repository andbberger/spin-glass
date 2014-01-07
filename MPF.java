package spin-glass;

import java.util.List;

/* To Do: 
   -write single bit flip gamma populator 
   -write energy gradients in Lattice
   -write gradient descent 
*/

/** Class implementing Minimum Probability Flow learning
 *  Initial implementation will only support Ising model,
 *  RBM will be supported as I better understand MPF
 *  @author Andrew Berger*/
public class MPF {

    MPF(List<State> observations) {
        _observations = observations;
    }

    /** Iterates through the obersations, filling gamma with an
     *  observation and it's bit flipped pairs.*/
    private void populateGamma() {
        for (State obs : _observations) {
            State[] bitFlips = obs.generateBitFlips();
            for (State flip : bitFlips) {
                _gamma.put(bitFlips, obs);
            }
        }
    }


    List<State> _observations;
    /** The transition probability matrix
     *  Entry ij is of the form g ij exp (1/2(E j (R) - E i (R)))
     *  R is the weight vector and g ij is 1 iff states ij are connected
     *  Only j in obeservations are present */
    SparseMatrix<double> _gamma; 
}