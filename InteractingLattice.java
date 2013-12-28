package spin-glass;

/** A lattice of interacting spins.
 *  AKA a hopfield network
 *  @author Andrew Berger */
public class InteractingLattice extends Lattice {

    /** A new interacting lattice with NUMSPINS.
     *  Initially all spins set to -1.*/
    InteractingLattice(int numSpins) {
        _weights = new Weighting(this);
        _size = numSpins;
        _lattice = new Spin[numSpins];
        for (int i = 0; i < numSpins; i++) {
            _lattice[i] = new Spin(false);
        }
        _rand = new Random();
    }
    
    @Override 
    public void converge() {
        /*some rethinking is needed here 
          At zero temperature it would be sufficient to 
          continue updating at random until we are reasonably sure
          a local min has been reached. 
          At non-zero temperature a similar strategy is not gauranteed to
          settle into a minimum. */
    }

    @Override
    public Lattice predecessor() {
        return this;
    }


}