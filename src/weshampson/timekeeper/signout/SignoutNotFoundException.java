
package weshampson.timekeeper.signout;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 19, 2014)
 * @since   0.3.0 (Nov 19, 2014)
 */
public class SignoutNotFoundException extends SignoutException {
    public SignoutNotFoundException() {
        super();
    }
    public SignoutNotFoundException(String message) {
        super(message);
    }
    public SignoutNotFoundException(Throwable cause) {
        super(cause);
    }
    public SignoutNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
