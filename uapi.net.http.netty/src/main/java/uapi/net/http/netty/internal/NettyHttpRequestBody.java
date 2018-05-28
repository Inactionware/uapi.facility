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

public class NettyHttpRequestBody implements IHttpRequestBody {

    private ByteBuf[] _bodyParts;

    NettyHttpRequestBody(ByteBuf... buffers) {
        this._bodyParts = buffers;
    }

    @Override
    public String getString(String encoder) {
        return null;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public int size() {
        return 0;
    }

//    public void appendBody(ByteBuf... buffers) {
//        ByteBuf buffer = httpContent.content();
//        if (buffer.isReadable()) {
//            this._bodyParts.add(buffer);
//        }
//        if (httpContent instanceof LastHttpContent) {
//            this._lastBody = true;
//        }
//    }
}
