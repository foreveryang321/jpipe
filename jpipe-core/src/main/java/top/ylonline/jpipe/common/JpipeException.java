package top.ylonline.jpipe.common;

/**
 * A generic exception known to the Jpipe project
 *
 * @author Created by YL on 2019/1/11
 */
public class JpipeException extends RuntimeException {
    private static final long serialVersionUID = 3122174655133694886L;

    public JpipeException(String message) {
        super(message);
    }

    public JpipeException(Throwable cause) {
        super(cause);
    }

    public JpipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
