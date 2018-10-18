/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

/**
 * The root cause for Resource Based Authentication framework
 */
public class ResourceException extends ParameterizedException {

    public static AuthenticationExceptionBuilder builder() {
        return new AuthenticationExceptionBuilder();
    }

    protected ResourceException(AuthenticationExceptionBuilder builder) {
        super(builder);
    }

    public static final class AuthenticationExceptionBuilder
            extends ExceptionBuilder<ResourceException, AuthenticationExceptionBuilder> {

        private AuthenticationExceptionBuilder() {
            super(ResourceErrors.CATEGORY, new ResourceErrors());
        }

        @Override
        protected ResourceException createInstance() {
            return new ResourceException(this);
        }
    }
}
