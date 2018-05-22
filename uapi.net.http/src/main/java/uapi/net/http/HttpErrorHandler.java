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
import uapi.net.IErrorHandler;

public class HttpErrorHandler implements IErrorHandler {

    private final IHttpResponse _response;

    HttpErrorHandler(
            final IHttpResponse response
    ) {
        this._response = response;
    }

    @Override
    public void handle(
            final Throwable t
    ) {
        ArgumentChecker.required(t, "t");

        this._response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        this._response.write(t.getMessage());
        this._response.flush();
    }
}
