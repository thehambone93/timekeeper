
package weshampson.timekeeper.tech;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 6, 2014)
 * @since   0.3.0 (Nov 6, 2014)
 */
public class TechNotFoundException extends TechException {
    public TechNotFoundException() {
        super();
    }
    public TechNotFoundException(String message) {
        super(message);
    }
    public TechNotFoundException(Throwable cause) {
        super(cause);
    }
    public TechNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
