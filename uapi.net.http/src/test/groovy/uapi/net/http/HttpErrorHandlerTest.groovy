/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http

import spock.lang.Specification

class HttpErrorHandlerTest extends Specification {

    def 'Test handle error'() {
        given:
        Throwable t = Mock(Throwable) {
            1 * getMessage() >> msg
        }
        IHttpResponse resp = Mock(IHttpResponse) {
            1 * setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            1 * flush()
        }

        when:
        HttpErrorHandler handler = new HttpErrorHandler(resp)

        then:
        handler.handle(t)

        where:
        msg     | ttt
        'aaa'   | null
    }
}
