/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

import uapi.IIdentifiable;
import uapi.net.INetEvent;

public interface IProtocol extends IIdentifiable<String> {

    @Override
    default String getId() {
        return type();
    }

    String type();

    boolean isSupport(INetEvent event);

    IProtocolDecoder decoder();

    IProtocolEncoder encoder();
}
