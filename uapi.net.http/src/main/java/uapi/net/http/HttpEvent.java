/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http;

import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;
import uapi.net.IErrorHandler;
import uapi.net.INetEvent;

public class HttpEvent implements INetEvent {

    public static final String TOPIC    = "HttpRequest";

    private final String _type;
    private final IHttpRequest _request;
    private final IHttpResponse _response;
    private final HttpErrorHandler _errHandler;

    public HttpEvent(
            final String type,
            final IHttpRequest request,
            final IHttpResponse response,
            final HttpErrorHandler errorHandler
    ) {
        ArgumentChecker.required(type, "type");
        ArgumentChecker.required(request, "request");
        ArgumentChecker.required(response, "response");
        ArgumentChecker.required(errorHandler, "errorHandler");

        this._type = type;
        this._request = request;
        this._response = response;
        this._errHandler = errorHandler;
    }

    @Override
    public String type() {
        return this._type;
    }

    @Override
    public String topic() {
        return TOPIC;
    }

    @Override
    public IHttpRequest request() {
        return this._request;
    }

    @Override
    public IHttpResponse response() {
        return this._response;
    }

    @Override
    public IErrorHandler errorHandler() {
        return this._errHandler;
    }

    @Override
    public String toString() {
        return StringHelper.makeString("HttpEvent: type={}", this._type);
    }
}
