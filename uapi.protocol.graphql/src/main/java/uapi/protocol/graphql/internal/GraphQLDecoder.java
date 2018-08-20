/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol.graphql.internal;

import uapi.net.http.HttpMethod;
import uapi.net.http.IHttpRequest;
import uapi.protocol.IProtocolDecoder;
import uapi.protocol.ResourceProcessing;

import java.util.List;

public class GraphQLDecoder implements IProtocolDecoder {

    @Override
    public ResourceProcessing decode(
            final ResourceProcessing resourceProcessing
    ) {
        IHttpRequest httpRequest = (IHttpRequest) resourceProcessing.originalRequest();
        if (httpRequest.method() == HttpMethod.GET) {
            List<String> queryParams = httpRequest.param("query");
            if (queryParams.size() != 1) {
                throw GraphQLException.builder()
                        .errorCode(GraphQLErrors.ONE_QUERY_PARAM_FOR_GET)
                        .build();
            }
            // todo: parse query parameter
        } else if (httpRequest.method() == HttpMethod.POST) {
            String body = httpRequest.body().getString("UTF-8");
            // todo: parse post body
        } else {
            throw GraphQLException.builder()
                    .errorCode(GraphQLErrors.UNSUPPORTED_METHOD)
                    .variables(httpRequest.method())
                    .build();
        }
        return resourceProcessing;
    }
}
