package uapi.net;

import uapi.IIdentifiable;

public interface INetListenerMeta extends IIdentifiable<String> {

    default String getId() {
        return type();
    }

    String type();

    INetListenerAttribute[] attributes();

    INetListenerInitializer newInitializer();
}
