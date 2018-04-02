/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;
import uapi.net.IRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtocolErrors extends FileBasedExceptionErrors<ProtocolException> {

    public static final int CATEGORY    = 0x0202;

    public static final int PROTOCOL_NOT_FOUND          = 1;
    public static final int ENCODER_NOT_DEFINED         = 2;
    public static final int DECODER_NOT_DEFINED         = 3;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(PROTOCOL_NOT_FOUND, ProtocolNotFound.KEY);
        keyCodeMapping.put(ENCODER_NOT_DEFINED, EncoderNotDefined.KEY);
        keyCodeMapping.put(DECODER_NOT_DEFINED, DecoderNotDefined.KEY);
    }

    @Override
    protected String getFile(ProtocolException exception) {
        if (exception.category() == CATEGORY) {
            return "/protocolErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(ProtocolException exception) {
        return keyCodeMapping.get(exception.errorCode());
    }

    /**
     * Error string template:
     *      There is not protocol for request - {}
     */
    public static final class ProtocolNotFound extends IndexedParameters<ProtocolNotFound> {

        private static final String KEY = "ProtocolNotFound";

        private IRequest _req;

        public ProtocolNotFound request(final IRequest request) {
            this._req = request;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._req };
        }
    }

    /**
     * Error string template:
     *      There is no encoder for request - {}
     */
    public static final class EncoderNotDefined extends IndexedParameters<EncoderNotDefined> {

        private static final String KEY = "EncoderNotDefined";

        private IRequest _req;

        public EncoderNotDefined request(final IRequest request) {
            this._req = request;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._req };
        }
    }

    /**
     * Error string template:
     *      There is no decoder for request - {}
     */
    public static final class DecoderNotDefined extends IndexedParameters<DecoderNotDefined> {

        private static final String KEY = "DecoderNotDefined";

        private IRequest _req;

        public DecoderNotDefined request(final IRequest request) {
            this._req = request;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._req };
        }
    }
}
