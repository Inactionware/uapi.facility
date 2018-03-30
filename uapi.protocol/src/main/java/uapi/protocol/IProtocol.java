package uapi.protocol;

import uapi.IIdentifiable;
import uapi.net.INetEvent;

public interface IProtocol extends IIdentifiable<String> {

    @Override
    default String getId() {
        return type();
    }

    String type();

    boolean isSupport(INetEvent event);

    IProtocolDecoder decoder();

    IProtocolEncoder encoder();
}
