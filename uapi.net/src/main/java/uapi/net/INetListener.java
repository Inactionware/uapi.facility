package uapi.net;

/**
 * A net listener is responsible to listen on specific network port to receive request and return response
 */
public interface INetListener {

    /**
     * Start up the net listener
     */
    void startUp();

    /**
     * Shut down the net listener
     */
    void shutDown();
}
