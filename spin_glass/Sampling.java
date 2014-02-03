package spin_glass;
    
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

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
        _bonds = new boolean[(int)(2 * _dim) - 1][(int)(2 * _dim) - 1];
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

    /** Picks a state according to Swendsen-Wang algorithm */
    private State pickState(State xcurr) {
        updateBonds(xcurr);
        hoshenkopelman();
        int x = _rand.nextInt((int) _dim);
        int y = _rand.nextInt((int) _dim);
        State xnew = flipRegion(xcurr, _labels[x][y]);
        return xnew;
    }
                                
    /** Flips all of the bits in REGION and returns that state.*/
    private State flipRegion(State xcurr, int region) {
        State xnew = new State(xcurr);
        for (int[] coord : _regions.get(region)) {
            xnew.flip(sqNum(coord[0], coord[1]));
        }
        return xnew;
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
                    pb = _glass.pbond(xcurr, sqNum(i, j), sqNum(x, y));
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

    /** Runs the Hoshen-Kopelman algorithm to find the largest contiguous regions*/
    private void hoshenkopelman() {
        _maxLabel = 0;
        _labelLinks = new int[(int)(_dim * _dim)];
        _labels = new int[(int)(2 * _dim) - 1][(int)(2 * _dim) - 1];
        boolean left, aboveLeft;
        for (int x = 0; x < (int)(_dim); x++) {
            for (int y = 0; y < (int)(_dim); y++) {
                if (x + y % 2 != 0 && _bonds[x][y] == true) {
                    left = false;
                    aboveLeft = false;
                    if (x > 1) {
                        left = _bonds[x - 2][y];
                    }
                    if (y > 1 && x > 0) {
                        aboveLeft = _bonds[x - 1][y - 1];
                    }
                    if (!left && !aboveLeft) {
                        _maxLabel += 1;
                        _labels[x][y] = _maxLabel;
                    } else if (left && !aboveLeft) {
                        _labels[x][y] = find(_labels[x - 2][y]);
                    } else if (!left && aboveLeft) {
                        _labels[x][y] = find(_labels[x - 1][y - 1]);
                    } else {
                        union(_labels[x - 2][y], _labels[x - 1][y - 1]);
                        _labels[x][y] = find(_labels[x - 2][y]);
                    }
                }
            }
        }
        
    }

    /** Fills the _regions for easy region selection.*/
    private void mapRegions() {
        _regions = new ArrayList<HashSet<int[]>>();
        for (int x = 0; x < (int)(_dim); x++) {
            for (int y = 0; y < (int)(_dim); y++) {
                if (x + y % 2 != 0) {
                    _regions.get(_labels[x][y]).add(new int[]{x, y});
                }
            }
        }
    }

    /** Returns the smallest equivalent label. */
    private int find(int x) {
        int r = x;
        while (_labelLinks[r] != r) {
            r = _labelLinks[r];
        }
        while (_labelLinks[x] != x) {
            int q = _labelLinks[x];
            _labelLinks[x] = r;
            x = q;
        }
        return r;
    }

    /** Makes label x and y equivalent.*/
    private void union(int x, int y) {
        _labelLinks[find(x)] = find(y);
    }

    /** Returns a mapping from Z^2 to Z^1*/
    private int sqNum(int i, int j) {
        return (int)(i*_dim) + j;
    }


    private ArrayList<HashSet<int[]>> _regions;
    private int _maxLabel;
    private int[][] _labels;
    private int[] _labelLinks;
    private boolean[][] _bonds;
    private double _dim;
    private Lattice _glass;
    private ArrayList<State> _samples;
    private Random _rand;
}