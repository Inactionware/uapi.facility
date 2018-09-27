/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

/**
 * The root cause for Resource Based Authentication framework
 */
public class AuthenticationException extends ParameterizedException {

    public static AuthenticationExceptionBuilder builder() {
        return new AuthenticationExceptionBuilder();
    }

    protected AuthenticationException(AuthenticationExceptionBuilder builder) {
        super(builder);
    }

    public static final class AuthenticationExceptionBuilder
            extends ExceptionBuilder<AuthenticationException, AuthenticationExceptionBuilder> {

        private AuthenticationExceptionBuilder() {
            super(AuthenticationErrors.CATEGORY, new AuthenticationErrors());
        }

        @Override
        protected AuthenticationException createInstance() {
            return new AuthenticationException(this);
        }
    }
}
