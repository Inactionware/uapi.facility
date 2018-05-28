/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpRequest
import spock.lang.Ignore
import spock.lang.Specification
import uapi.event.IEventBus

@Ignore
class HttpRequestHandlerTest extends Specification {

    def 'test crate instance'() {
        when:
        new HttpRequestHandler(Mock(IEventBus), 'source')

        then:
        noExceptionThrown()
    }

    def 'test channel read'() {
        given:
        def ctx = Mock(ChannelHandlerContext)
        def msg = Mock(HttpRequest)
        def handler = new HttpRequestHandler(Mock(IEventBus), 'source')

        when:
        handler.channelRead(ctx, msg)

        then:
        noExceptionThrown()

    }
}
