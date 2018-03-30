package uapi.protocol;

import uapi.GeneralException;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.common.ArgumentChecker;
import uapi.net.INetEvent;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;

@Service
@Action
public class InitProcessing {

    @Inject
    protected IProtocolRegistry _protoReg;

    @ActionDo
    public ResourceProcessing process(final INetEvent event) {
        ArgumentChecker.required(event, "event");

        IProtocol proto = this._protoReg.find(event);
        if (proto == null) {
            throw new GeneralException("Can't find protocol for event - {}", event);
        }

        return new ResourceProcessing(event, proto);
    }
}
