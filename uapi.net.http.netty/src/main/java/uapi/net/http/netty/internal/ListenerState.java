package uapi.net.http.netty.internal;

public enum ListenerState {

    Stopped(0),
    Starting(10),
    Started(20),
    Stopping(30);

    private int _value;

    int value() {
        return this._value;
    }

    ListenerState(int value) {
        this._value = value;
    }
}
