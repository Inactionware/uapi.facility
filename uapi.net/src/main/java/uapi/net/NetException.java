/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

/**
 * The root exception of net framework
 */
public class NetException extends ParameterizedException {

    public static NetExceptionBuilder builder() {
        return new NetExceptionBuilder();
    }

    protected NetException(ExceptionBuilder builder) {
        super(builder);
    }

    public static final class NetExceptionBuilder
            extends ExceptionBuilder<NetException, NetExceptionBuilder> {

        private NetExceptionBuilder() {
            super(NetErrors.CATEGORY, new NetErrors());
        }

        @Override
        protected NetException createInstance() {
            return new NetException(this);
        }
    }
}
