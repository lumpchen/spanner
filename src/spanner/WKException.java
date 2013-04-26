/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

/**
 *
 * @author lim16
 */
public class WKException extends Exception {

    public WKException(Class claz, String message, Throwable cause) {
        super(claz.getName() + ": " + message, cause);
    }

    public WKException(Exception ex) {
        super(ex);
    }
}
