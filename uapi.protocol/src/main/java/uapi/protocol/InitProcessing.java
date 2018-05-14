/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

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
            throw ProtocolException.builder()
                    .errorCode(ProtocolErrors.PROTOCOL_NOT_FOUND)
                    .variables(event.request())
                    .build();
        }

        return new ResourceProcessing(event, proto);
    }
}
