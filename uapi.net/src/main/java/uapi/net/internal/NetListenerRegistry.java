package uapi.net.internal;

import uapi.net.INetListenerInitializer;
import uapi.net.INetListenerMeta;
import uapi.net.INetListenerRegistry;

public class NetListenerRegistry implements INetListenerRegistry {
    @Override
    public void register(INetListenerMeta netListenerMeta) {

    }

    @Override
    public void unregister(String type) {

    }

    @Override
    public INetListenerInitializer getInitializer(String type) {
        return null;
    }
}
