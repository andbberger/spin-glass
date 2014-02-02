package spin-glass;
    
import java.util.Random;
import java.util.ArrayList;

/** Class containing Swendsen-Wang sampling with utility methods
 *  @author Andrew Berger */
public class Sampling {
    /** A new sampler. GLASS can be anything, but the focus is on 
     *  pairwise interactions */
    Sampling(Lattice glass) {
        _glass = glass;
        _samples = new ArrayList<State>;
        _rand = new Random();
    }

    /** Returns the non-normalized probability of realizing state X.*/
    private double pi(State x) {
        return Math.exp(_glass.getEnergy(x));
    }

    private void sample(int nsamples, State initial) {
        double pcurr, pnew;
        State xcurr = initial;
        for (int u; u < nsamples; u++) {
            pcurr = pi(xcurr);
            State xnew = pickState(xcurr);
            pnew = pi(xnew);
            double ptransition = Math.min(1, pnew/pcurr);
            if (_rand.nextDouble() < ptransition) {
                xcurr = xnew;
                _samples.add(xcurr);
            }
        }
    }

    /** Picks a spin at random and flips all contiguous spins. */
    private State pickState(State xcurr) {
        
    }

    private Lattice _glass;
    private ArrayList<State> _samples;
    private Random _rand;
}