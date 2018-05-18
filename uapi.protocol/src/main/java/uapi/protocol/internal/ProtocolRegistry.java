/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol.internal;

import uapi.common.ArgumentChecker;
import uapi.log.ILogger;
import uapi.net.INetEvent;
import uapi.protocol.IProtocol;
import uapi.protocol.IProtocolRegistry;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.List;
import java.util.Map;

@Service(IProtocolRegistry.class)
public class ProtocolRegistry implements IProtocolRegistry {

    @Inject
    protected ILogger _logger;

    @Inject
    @Optional
    protected Map<String, IProtocol> _protocols;

    @Override
    public IProtocol find(INetEvent event) {
        ArgumentChecker.required(event, "event");

        List<IProtocol> protocols = Looper.on(this._protocols.entrySet())
                .map(Map.Entry::getValue)
                .filter(protocol -> protocol.isSupport(event))
                .toList();
        if (protocols.size() == 0) {
            return null;
        } else if (protocols.size() > 1) {
            this._logger.warn(
                    "Found multiple protocol support event [{}] - {}, first will be used.",
                    event, protocols);
        }

        return protocols.get(0);
    }
}
