
package weshampson.timekeeper.signout;

/**
 *
 * @author  Wes Hampson
 * @version 0.2.0 (Jul 29, 2014)
 * @since   0.2.0 (Jul 29, 2014)
 */
public class SignoutException extends Exception {
    public SignoutException() {
        super();
    }
    public SignoutException(String message) {
        super(message);
    }
    public SignoutException(Throwable cause) {
        super(cause);
    }
    public SignoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
