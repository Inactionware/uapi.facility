/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import uapi.common.ArgumentChecker;
import uapi.net.http.HttpErrors;
import uapi.net.http.HttpException;
import uapi.net.http.HttpVersion;

public class HttpVersionConverter {

    public static io.netty.handler.codec.http.HttpVersion toNettyVersion(
            final HttpVersion httpVersion
    ) {
        ArgumentChecker.required(httpVersion, "httpVersion");
        switch (httpVersion) {
            case V_1_0:
                return io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
            case V_1_1:
                return io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
            default:
                throw HttpException.builder()
                        .errorCode(HttpErrors.UNSUPPORTED_HTTP_VERSION)
                        .variables(httpVersion.name())
                        .build();
        }
    }

    public static HttpVersion toUapiVersion(
            final io.netty.handler.codec.http.HttpVersion nettyHttpVersion
    ) {
        ArgumentChecker.required(nettyHttpVersion, "nettyHttpVersion");
        if (io.netty.handler.codec.http.HttpVersion.HTTP_1_0.equals(nettyHttpVersion)) {
            return HttpVersion.V_1_0;
        } else if (io.netty.handler.codec.http.HttpVersion.HTTP_1_1.equals(nettyHttpVersion)) {
            return HttpVersion.V_1_1;
        } else {
            throw HttpException.builder()
                    .errorCode(HttpErrors.UNSUPPORTED_HTTP_VERSION)
                    .variables(nettyHttpVersion.text())
                    .build();
        }
    }

    private HttpVersionConverter() { }
}
