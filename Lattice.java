package spin-glass;

import java.util.Random;

/** A lattice of possibly interacting spins.
 *  Lattices are not resizable. 
 *  @author Andrew Berger*/
abstract class Lattice {

    /** Updates this lattice.
     *  If non-interacting, simply updates each spin at
     *   the set temperature.
     *  If interacting and non-zero temp, performs simulated annealing from the 
     *   set temperature to zero.
     *  If interacting at zero temp, Iterates until a fixed point is reached. */
    public abstract void converge();

    /** Returns the lattice that interacts with the lattice I implement. */
    public abstract Lattice predecessor();

    /** Returns the number of spins in this lattice.*/
    public int latticeSize() {
        return _size;
    }

    /** Updates spin at IND, summing the activation function 
     *  over all predecessors of IND. */
    public void updateSpin(int ind) {
        if (_temperature == 0) {
            zeroTempSpinUpdate(ind);
        } else {
            boltzmannSpinUpdate(ind);
        }
    }

    /** Returns the current activation of the spin at IND.*/
    public int getSpin(int ind) {
        return _lattice[ind].get();
    }

    /** Returns the threshold of spin at IND. */
    public int getThreshold(int ind) {
        return _lattice[ind].getThreshold();
    }

    public double getEnergy() {
        if (!EnergySet) {
            _energy = energy();
        }
        return _energy = energy;
    }

    /** Sets the spin at IND to 1 iff ACTIVATED is true. */
    public void setSpin(int ind, boolean activated) {
        _lattice[ind].set(activated);
    }

    /** Constructs a long representing the lattice state.
     *  Not a very good hash,important property is that 
     *   one spin flip will always produce a different number.*/
    public long representation() {
        long repr = 0;
        int currSpin = 0;
        for (int k = 0; k < latticeSize(); k++ % 64) {
            int bit = getSpin(currSpin) > 0 ? 1 : 0;
            currSpin += 1;
            repr ^= (bit << k);
        }
        return repr;
    }

    /** Setting TEMP to zero yields a fully deterministic lattice.
     *  Otherwise uses a boltzmann distribution. */
    public void setTemperature(double temp) {
        _setTemperature = temp;
        _temperature = temp;
    }

    /** Computes and stores the energy, which is subsequently only updated
     *  Through calls to the faster energy diff.*/
    public void setEnergy() {
        _energy = energy();
    }

    /** Fully deterministic spin updater.
     *  Always settles into a local min. */
    private void zeroTempSpinUpdate(int ind) {
        double sum = 0;
        for (int i = 0; i < predecessor().latticeSize(); i++) {
            sum += _weights[i][ind] * predecessor().getSpin(ind);
        }
        if (sum > getThreshold(ind)) {
            setSpin(ind, true);
        } else {
            setSpin(ind, false);
        }
    }

    /** Result of E(spin at ind = -1) - E(spin at ind = 1) */
    private double energyDiff(int ind) {
        double e = 0;
        for (int i = 0; i < predecessor().latticeSize(); i++) {
            e += _weights[i][ind] * predecessor().getSpin(ind);
        }
        // check the sign here
        e -= getThreshold(ind);
        return e;
    }    

    /** Returns the total energy of this state. 
     *  Call once and keep track of energy change with energyDiff,*/
    private double energy() {
        double e = 0;
        for (int i = 0; i < predecessor().latticeSize(); i++) {
            for (int j = 0; i < latticeSize(); j++) {
                e -= _weights[i][j] * predecessor().getSpin(i) * getSpin(j); 
            }
        }
        e = e / 2;
        //not general, RBM sum over hidden layer too
        for (int i = 0; i < latticeSize(); i++) {
            e += getThreshold(i);
        }
        energySet = true;
        return e;
    }

    /** Returns the derivative of the Energy with respect
     *  To weight i j.
     *  In the case of the boltzmann machine this takes the very simple
     *  form -1/2 Si Sj */
    public double dEdW(int i, int j) {
        return (double) -.5 * predecessor().getSpin(i) * getSpin(j);
    }

    /** Given a GRADIENT corresponding to MPF eq 16,
     *  Updates our weighting to a better value. */
    public void updateWeights(double[][] gradient) {
        _weights.gradientDescent(gradient, EPS);
    }
    
    /** Probabilistic spin updater.
     *  Has a probability to settle into a higher energy state,
     *  which is proportional to the temperature of the system
     *  In this way can 'tunnel' out of a local min. */
    private void boltzmannSpinUpdate(int ind) {
        //double overflow to be wary of???
        double boltzmannFactor = Math.pow(Math.E, -(energyDiff(ind) / _temperature));
        double probOn = 1 / (1 + boltzmannFactor);
        if (_rand.nextDouble() <= probOn) {
            setSpin(ind, true);
        } else {
            setSpin(ind, false);
        }
    }

    /** Our lattice is in equilibrium with a heat bath at TEMPERATURE.
     *  Always >= 0.
     *  Setting temperature to 0 yields a fully deterministic lattice.
     *  For temp > 0, the lattice has a probability to 'tunnel' out 
     *  of a local min*/
    private double _temperature;
    /** Temperature which simulated annealing is started from 
     *  and RBM update ran at. */
    private double _setTemperature;
    /** The spins in this lattice.*/
    private Spin[] _lattice;
    /** The weights between spins in this lattice.*/
    private Weighting _weights;
    /** Number of spins in this lattice. */
    private int _size;
    /** The PRNG. */
    private Random _rand; 
    /** The current energy of the lattice. */
    private double _energy;
    /** True iff the energy has been computed. */
    private boolean energySet = false;
}