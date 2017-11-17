package uapi.net.telnet.internal;

import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;

@NetListener
public class TelnetListener {

    private static final int DEFAULT_PORT   = 23;

    @Attribute(name = "port", isRequired = false)
    int _port = DEFAULT_PORT;
}
