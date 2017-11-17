package uapi.net;

public interface INetListenerMeta {

    String type();

    INetListenerAttribute[] attributes();

    INetListenerInitializer newInitializer();
}
