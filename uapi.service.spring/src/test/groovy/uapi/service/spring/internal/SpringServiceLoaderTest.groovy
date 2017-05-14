/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.service.spring.internal

import spock.lang.Specification

/**
 * Unit test for SpringServiceLoader
 */
class SpringServiceLoaderTest extends Specification {

    def 'Test init method'() {
        given:
        def loader = new SpringServiceLoader()
        loader._cfgFile = 'appctx.xml'
        loader.init()

        expect:
        TestBean bean = loader.load(svcId, null)
        bean != null
        bean.name == name


        where:
        svcId       | name
        'testBean'  | 'MyName'
    }
}
