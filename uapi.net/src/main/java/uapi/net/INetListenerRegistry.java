package uapi.net;

public interface INetListenerRegistry {

    void register(INetListenerMeta netListenerMeta);

    void unregister(String type);

    INetListenerInitializer getInitializer(String type);
}
