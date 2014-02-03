package spin_glass;

/** An unchecked exception that represents internally any kind of
 *  state error.
 *  @author Andrew Berger */
public class StateException extends RuntimeException {

    /** A StateException with no message. */
    StateException() {
    }

    /** A StateException for which .getMessage() is MSG. */
    StateException(String msg) {
        super(msg);
    }   
}