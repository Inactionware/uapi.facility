package uapi.net.http.internal;

import uapi.net.INetListener;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.net.http.HttpAttributes;

@NetListener(type = HttpAttributes.TYPE)
public class HttpListener implements INetListener {

    @Attribute(HttpAttributes.HOST)
    protected String _host;

    @Attribute(HttpAttributes.PORT)
    protected int _port;

    @Override
    public void startUp() throws Exception {

    }

    @Override
    public void shutDown() throws Exception {

    }
}
