package net.engio.mbassy.bus.error;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Publication errors are created when object publication fails
 * for some reason and contain details as to the cause and location
 * where they occurred.
 * <p/>
 *
 * @author bennidi
 *         Date: 2/22/12
 *         Time: 4:59 PM
 */
public class PublicationError {

    // Internal state
    private Throwable cause;
    private String message;
    private Method handler;
    private Object listener;
    private Object[] publishedObjects;


    /**
     * Default constructor.
     */
    public PublicationError() {
        super();
    }

    /**
     * @return The Throwable giving rise to this PublicationError.
     */
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * Assigns the cause of this PublicationError.
     *
     * @param cause A Throwable which gave rise to this PublicationError.
     * @return This PublicationError.
     */
    public PublicationError setCause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public PublicationError setMessage(String message) {
        this.message = message;
        return this;
    }

    public Method getHandler() {
        return this.handler;
    }

    public PublicationError setHandler(Method handler) {
        this.handler = handler;
        return this;
    }

    public Object getListener() {
        return this.listener;
    }

    public PublicationError setListener(Object listener) {
        this.listener = listener;
        return this;
    }

    public Object[] getPublishedObject() {
        return this.publishedObjects;
    }

    public PublicationError setPublishedObject(Object[] publishedObjects) {
        this.publishedObjects = publishedObjects;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String newLine = System.getProperty("line.separator");
        return "PublicationError{" +
                newLine +
                "\tcause=" + this.cause +
                newLine +
                "\tmessage='" + this.message + '\'' +
                newLine +
                "\thandler=" + this.handler +
                newLine +
                "\tlistener=" + this.listener +
                newLine +
                "\tpublishedObject=" + Arrays.deepToString(this.publishedObjects) +
                '}';
    }
}
