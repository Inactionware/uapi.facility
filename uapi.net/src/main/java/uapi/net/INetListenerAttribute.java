package uapi.net;

/**
 * A net listener attribute is used to initialize net listener
 */
public interface INetListenerAttribute {

    /**
     * The attribute name
     *
     * @return  The name of attribute
     */
    String name();

    /**
     * Indicate the attribute is required
     *
     * @return  True means the attribute is required otherwise is optional
     */
    boolean isRequired();

    /**
     * Indicate the attribute type
     *
     * @return  The type of attribute
     */
    AttributeType type();
}
