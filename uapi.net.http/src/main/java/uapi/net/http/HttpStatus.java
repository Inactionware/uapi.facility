/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http;

public enum HttpStatus {

    OK(200),
    CREATED(201),
    ACCEPTED(202),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    CONFLICT(409),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int _code;

    HttpStatus(int code) {
        this._code = code;
    }

    public int getCode() {
        return this._code;
    }
}
