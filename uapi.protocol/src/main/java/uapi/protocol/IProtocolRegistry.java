package uapi.protocol;

import uapi.net.INetEvent;

public interface IProtocolRegistry {

    IProtocol find(INetEvent event);
}
