package spin_glass;
    
import java.util.Random;
import java.util.ArrayList;

/** Class containing Swendsen-Wang sampling with utility methods
 *  @author Andrew Berger */
public class Sampling {
    /** A new sampler. GLASS can be anything, but the focus is on 
     *  pairwise interactions */
    Sampling(Lattice glass) {
        _glass = glass;
        _samples = new ArrayList<State>();
        _rand = new Random();        
        _dim = Math.sqrt(_glass.latticeSize());
        if (_dim != Math.floor(_dim)) {
            throw new StateException();
        }
        _bonds = new boolean[(int)(2*_dim) - 1][(int)(2*_dim) - 1];
    }

    /** Returns the non-normalized probability of realizing state X.*/
    private double pi(State x) {
        return Math.exp(_glass.getEnergy(x));
    }

    private void sample(int nsamples, State initial) {
        double pcurr, pnew;
        State xcurr = initial;
        for (int u = 0 ; u < nsamples; u++) {
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
        return null;
    }
    
    /** Core routine of Swendsen-Wang. Updates the bond matrix
     *  according to a certain probability.*/
    private void updateBonds(State xcurr) {
        double pb = 0;
        int x = 0;
        int y = 0;
        for (int i = 0; i < _dim; i++) {
            for (int j = 0; j < _dim; j++) {
                coord:
                for (int k = 0; k < 1; k++) {
                    if (k == 0) {
                        //Spin to right of i,j
                        if(j != _dim - 1) {
                            x = i;
                            y = j + 1;
                        } else {
                            break coord;
                        }
                    } else {
                        //Spin below i,j
                        if (i != _dim - 1) {
                            x = i + 1; 
                            y = j;
                        } else {
                            break coord;
                        }
                    }
                    pb = _glass.pbond(xcurr, mapSquaredCoords(i, j), mapSquaredCoords(x, y));
                    if (pb > 1) {
                        break coord;
                    } else if(_rand.nextDouble() < pb) {
                        _bonds[2*i - x][2*j - y] = true;
                    } else {
                        _bonds[2*i - x][2*j - y] = false;
                    }
                }
            }
        }
    }


    /** Returns a mapping from Z^2 to Z^1*/
    private int mapSquaredCoords(int i, int j) {
        return (int)(i*_dim) + j;
    }
    
    private boolean[][] _bonds;
    private double _dim;
    private Lattice _glass;
    private ArrayList<State> _samples;
    private Random _rand;
}