package uapi.protocol;

import uapi.common.ArgumentChecker;
import uapi.net.INetEvent;
import uapi.net.IRequest;
import uapi.net.IResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourceProcessing {

    private final INetEvent _event;
    private final List<ResourceOperation> _resOps;
    private final ResourceResult _resResult;

    public ResourceProcessing(final INetEvent event) {
        ArgumentChecker.required(event, "event");

        this._event = event;
        this._resOps = new ArrayList<>();
        this._resResult = new ResourceResult();
    }

    public IRequest originalRequest() {
        return this._event.request();
    }

    public IResponse originalResponse() {
        return this._event.response();
    }

    public void addOperation(final ResourceOperation resourceOperation) {
        ArgumentChecker.required(resourceOperation, "resourceOperation");
        this._resOps.add(resourceOperation);
    }

    public Iterator<ResourceOperation> operationIterator() {
        return this._resOps.iterator();
    }

    public ResourceResult result() {
        return this._resResult;
    }
}
