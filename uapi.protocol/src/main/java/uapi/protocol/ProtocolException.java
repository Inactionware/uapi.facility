/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

public class ProtocolException extends ParameterizedException {

    public static ProtocolExceptionBuilder builder() {
        return new ProtocolExceptionBuilder();
    }

    protected ProtocolException(ExceptionBuilder builder) {
        super(builder);
    }

    public static final class ProtocolExceptionBuilder
            extends ExceptionBuilder<ProtocolException, ProtocolExceptionBuilder> {

        public ProtocolExceptionBuilder() {
            super(ProtocolErrors.CATEGORY, new ProtocolErrors());
        }

        @Override
        protected ProtocolException createInstance() {
            return new ProtocolException(this);
        }
    }
}
