/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import io.netty.buffer.ByteBuf;
import uapi.net.http.IHttpRequestBody;

import java.nio.charset.Charset;
import java.util.Arrays;

public class NettyHttpRequestBody implements IHttpRequestBody {

    private byte[] _bodyBytes;

    NettyHttpRequestBody(ByteBuf... buffers) {
        for (ByteBuf bodyPart : buffers) {
            int len = bodyPart.readableBytes();
            byte[] arr = new byte[len];
            bodyPart.readBytes(arr);
            if (this._bodyBytes == null) {
                this._bodyBytes = arr;
            } else {
                this._bodyBytes = Arrays.copyOf(this._bodyBytes, this._bodyBytes.length + arr.length);
                System.arraycopy(arr, 0, this._bodyBytes, this._bodyBytes.length - 1, arr.length);
            }
        }
    }

    @Override
    public String getString(String encoder) {
        return new String(this._bodyBytes, Charset.forName(encoder));
    }

    @Override
    public byte[] getBytes() {
        return this._bodyBytes;
    }

    @Override
    public int size() {
        return this._bodyBytes.length;
    }
}
