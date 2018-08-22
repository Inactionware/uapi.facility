/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal

import spock.lang.Specification
import uapi.net.http.netty.internal.ConstantConverter

class ConstantConverterTest extends Specification {

    def testConvertNettyVersion() {
        when:
        def ver = ConstantConverter.toNetty(uapiVer)

        then:
        ver == nettyVer

        where:
        uapiVer                             | nettyVer
        uapi.net.http.HttpVersion.V_1_0     | io.netty.handler.codec.http.HttpVersion.HTTP_1_0
        uapi.net.http.HttpVersion.V_1_1     | io.netty.handler.codec.http.HttpVersion.HTTP_1_1
    }

    def testConvertUapiVersion() {
        when:
        def ver = ConstantConverter.toUapi(nettyVer)

        then:
        ver == uapiVer

        where:
        nettyVer                                            | uapiVer
        io.netty.handler.codec.http.HttpVersion.HTTP_1_0    | uapi.net.http.HttpVersion.V_1_0
        io.netty.handler.codec.http.HttpVersion.HTTP_1_1    | uapi.net.http.HttpVersion.V_1_1
    }

    def testConvertToNettyMethod() {
        when:
        def method = ConstantConverter.toNetty(uapiMethod)

        then:
        method == nettyMethod

        where:
        uapiMethod                      | nettyMethod
        uapi.net.http.HttpMethod.GET    | io.netty.handler.codec.http.HttpMethod.GET
        uapi.net.http.HttpMethod.POST   | io.netty.handler.codec.http.HttpMethod.POST
        uapi.net.http.HttpMethod.DELETE | io.netty.handler.codec.http.HttpMethod.DELETE
        uapi.net.http.HttpMethod.PATCH  | io.netty.handler.codec.http.HttpMethod.PATCH
        uapi.net.http.HttpMethod.PUT    | io.netty.handler.codec.http.HttpMethod.PUT
    }

    def testConvertToUapiMethod() {
        when:
        def method = ConstantConverter.toUapi(nettyMethod)

        then:
        method == uapiMethod

        where:
        nettyMethod                                         | uapiMethod
        io.netty.handler.codec.http.HttpMethod.GET          | uapi.net.http.HttpMethod.GET
        io.netty.handler.codec.http.HttpMethod.POST         | uapi.net.http.HttpMethod.POST
        io.netty.handler.codec.http.HttpMethod.DELETE       | uapi.net.http.HttpMethod.DELETE
        io.netty.handler.codec.http.HttpMethod.PATCH        | uapi.net.http.HttpMethod.PATCH
        io.netty.handler.codec.http.HttpMethod.PUT          | uapi.net.http.HttpMethod.PUT
    }
}
