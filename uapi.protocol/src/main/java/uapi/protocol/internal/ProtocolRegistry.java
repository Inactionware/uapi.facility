/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol.internal;

import uapi.GeneralException;
import uapi.common.ArgumentChecker;
import uapi.log.ILogger;
import uapi.net.INetEvent;
import uapi.protocol.IProtocol;
import uapi.protocol.IProtocolRegistry;
import uapi.rx.Looper;
import uapi.service.IServiceLifecycle;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(IProtocolRegistry.class)
public class ProtocolRegistry implements IProtocolRegistry, IServiceLifecycle {

    @Inject
    protected ILogger _logger;

    @Inject
    @Optional
    protected Map<String, IProtocol> _protocols = new HashMap<>();

    @Override
    public IProtocol find(INetEvent event) {
        ArgumentChecker.required(event, "event");

        var protocols = Looper.on(this._protocols.entrySet())
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

    @Override
    public void onActivate() {
        // do nothing
    }

    @Override
    public void onDeactivate() {
        // do nothing
    }

    @Override
    public void onDependencyInject(String serviceId, Object service) {
        if (IProtocol.class.getCanonicalName().equals(serviceId) && service instanceof IProtocol) {
            var proto = (IProtocol) service;
            this._protocols.put(proto.getId(), proto);
        } else {
            throw new GeneralException(
                    "Unsupported dependency injection - {}, {}", service, service.getClass().getCanonicalName());
        }
    }
}
