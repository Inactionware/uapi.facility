package uapi.net.http.internal;

import uapi.net.INetListener;
import uapi.net.NetException;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.net.http.HttpAttributes;

@NetListener(type = HttpAttributes.TYPE)
public class HttpListener implements INetListener {

    @Attribute(value = HttpAttributes.HOST, isRequired = true)
    protected String _host;

    @Attribute(value = HttpAttributes.PORT, isRequired = true)
    protected int _port;

    @Override
    public void startUp() throws NetException {

    }

    @Override
    public void shutDown() throws NetException {

    }
}
