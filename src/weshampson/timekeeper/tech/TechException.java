/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package weshampson.timekeeper.tech;

/**
 * Thrown when an error related to a {@link weshampson.timekeeper.tech.Tech}
 * object occurs.
 * 
 * @author  Wes Hampson
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 20, 2014)
 */
public class TechException extends Exception {

    /**
     * Created a new exception with a {@code null} message.
     */
    public TechException() {
        super();
    }

    /**
     * Creates a new exception with a specific message.
     * @param message exception details
     */
    public TechException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with a specific cause.
     * @param cause exception cause
     */
    public TechException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new exception with a specific message and cause.
     * @param message exception details
     * @param cause exception cause
     */
    public TechException(String message, Throwable cause) {
        super(message, cause);
    }
}
