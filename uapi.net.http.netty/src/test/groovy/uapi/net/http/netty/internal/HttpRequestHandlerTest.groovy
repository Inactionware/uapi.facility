/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.LastHttpContent
import spock.lang.Ignore
import spock.lang.Specification
import uapi.common.Capacity
import uapi.event.IEventBus

class HttpRequestHandlerTest extends Specification {

    def 'test crate instance'() {
        given:
        IEventBus eventBus = Mock(IEventBus)

        when:
        new HttpRequestHandler(eventBus, srcName, Capacity.parse(cap))

        then:
        noExceptionThrown()

        where:
        srcName     | cap
        'source'    | '1MB'
    }

    def 'test channel read on empty body'() {
        given:
        def ctx = Mock(ChannelHandlerContext)

        HttpHeaders httpHeaders = new MockHeaders()

        def req = Mock(HttpRequest) {
            protocolVersion() >> protoVer
            method() >> reqMethod
            uri() >> reqUri
            headers() >> httpHeaders
        }

        def reqContent = Mock(HttpContent) {
            content() >> Mock(ByteBuf)
        }

        def eventBus = Mock(IEventBus)
        def handler = new HttpRequestHandler(eventBus, srcName, Capacity.parse(cap))

        when:
        handler.channelRead(ctx, req)
        handler.channelRead(ctx, reqContent)

        then:
        noExceptionThrown()

        where:
        srcName     | cap       | protoVer              | reqMethod         | reqUri
        'source'    | '1MB'     | HttpVersion.HTTP_1_1  | HttpMethod.GET    | '/test?a=b'
    }

    @Ignore
    def 'test channel read on small body'() {
        given:
        def ctx = Mock(ChannelHandlerContext)

        HttpHeaders httpHeaders = new MockHeaders()

        def req = Mock(HttpRequest) {
            protocolVersion() >> protoVer
            method() >> reqMethod
            uri() >> reqUri
            headers() >> httpHeaders
        }

        def reqContent = Mock(HttpContent) {
            content() >> Mock(ByteBuf) {
                readableBytes() >> 100
                readBytes()
            }
        }

        def lastReqContent = Mock(LastHttpContent) {

        }

        def eventBus = Mock(IEventBus)
        def handler = new HttpRequestHandler(eventBus, srcName, Capacity.parse(cap))

        when:
        handler.channelRead(ctx, req)
        handler.channelRead(ctx, reqContent)

        then:
        noExceptionThrown()

        where:
        srcName     | cap       | protoVer              | reqMethod         | reqUri
        'source'    | '1MB'     | HttpVersion.HTTP_1_1  | HttpMethod.GET    | '/test?a=b'
    }

    private class MockHeaders extends HttpHeaders {

        @Override
        String get(String name) {
            return null
        }

        @Override
        Integer getInt(CharSequence name) {
            return null
        }

        @Override
        int getInt(CharSequence name, int defaultValue) {
            return 0
        }

        @Override
        Short getShort(CharSequence name) {
            return null
        }

        @Override
        short getShort(CharSequence name, short defaultValue) {
            return 0
        }

        @Override
        Long getTimeMillis(CharSequence name) {
            return null
        }

        @Override
        long getTimeMillis(CharSequence name, long defaultValue) {
            return 0
        }

        @Override
        List<String> getAll(String name) {
            return null
        }

        @Override
        List<Map.Entry<String, String>> entries() {
            return null
        }

        @Override
        boolean contains(String name) {
            return false
        }

        @Override
        Iterator<Map.Entry<String, String>> iterator() {
            return ['a': 'b'].iterator()
        }

        @Override
        Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
            return ['a': 'b'].iterator()
        }

        @Override
        boolean isEmpty() {
            return false
        }

        @Override
        int size() {
            return 0
        }

        @Override
        Set<String> names() {
            return null
        }

        @Override
        HttpHeaders add(String name, Object value) {
            return null
        }

        @Override
        HttpHeaders add(String name, Iterable<?> values) {
            return null
        }

        @Override
        HttpHeaders addInt(CharSequence name, int value) {
            return null
        }

        @Override
        HttpHeaders addShort(CharSequence name, short value) {
            return null
        }

        @Override
        HttpHeaders set(String name, Object value) {
            return null
        }

        @Override
        HttpHeaders set(String name, Iterable<?> values) {
            return null
        }

        @Override
        HttpHeaders setInt(CharSequence name, int value) {
            return null
        }

        @Override
        HttpHeaders setShort(CharSequence name, short value) {
            return null
        }

        @Override
        HttpHeaders remove(String name) {
            return null
        }

        @Override
        HttpHeaders clear() {
            return null
        }
    }
}
