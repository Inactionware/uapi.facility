/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol.graphql.internal;

import uapi.protocol.IProtocolEncoder;
import uapi.protocol.ResourceProcessing;

public class GraphQLJsonEnncoder implements IProtocolEncoder {
    @Override
    public ResourceProcessing encode(ResourceProcessing resourceProcessing) {
        return null;
    }

    @Override
    public ResourceProcessing encodeError(Exception exception, ResourceProcessing processing) {
        return null;
    }
}
