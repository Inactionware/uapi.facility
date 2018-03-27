package uapi.net;

import uapi.event.IEvent;

public interface INetEvent extends IEvent {

    /**
     * Get type based on TCP/UDP
     *
     * @return  The network type
     */
    String type();

    IRequest request();

    IResponse response();
}
