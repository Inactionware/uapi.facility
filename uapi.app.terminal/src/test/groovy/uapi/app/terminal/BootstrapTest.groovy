/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.app.terminal

import spock.lang.Ignore
import spock.lang.Specification
import uapi.app.AppException
import uapi.app.SystemBootstrap
import uapi.app.internal.AppServiceLoader
import uapi.app.internal.SystemShuttingDownEvent
import uapi.app.internal.SystemStartingUpEvent
import uapi.app.terminal.internal.CliConfigProvider
import uapi.event.IEventBus
import uapi.service.IRegistry
import uapi.service.IService
import uapi.service.ITagged

/**
 * Unit tests for Bootstrap
 */
class BootstrapTest extends Specification {

    def 'Test start up with zero registry'() {
        given:
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> []
        }

        when:
        Bootstrap.main([] as String[])

        then:
        thrown(AppException)
    }

    def 'Test start up with more registry'() {
        given:
        def registry = Mock(IRegistryService)
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> [registry, registry]
        }

        when:
        Bootstrap.main([] as String[])

        then:
        thrown(AppException)
    }

    def 'Test start up when registry cannot be initialized'() {
        given:
        def registry = Mock(IRegistryService)
        registry.findService(IRegistry.class) >> null
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> [registry]
        }

        when:
        Bootstrap.main([] as String[])

        then:
        thrown(AppException)
    }

    def 'Test start up when cli config provider service was not found'() {
        given:
        def registry = Mock(IRegistryService)
        registry.findService(IRegistry.class) >> registry
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> [registry]
        }

        when:
        Bootstrap.main([] as String[])

        then:
        thrown(AppException)
    }

    def 'Test start up'() {
        given:
        def registry = Mock(IRegistryService)
        1 * registry.findService(IRegistry.class) >> registry
        3 * registry.findService(IEventBus.class) >> Mock(IEventBus) {
            1 * fire(_ as SystemStartingUpEvent)
            1 * fire(_ as SystemShuttingDownEvent, true)
        }
        registry.findService(CliConfigProvider.class) >> Mock(CliConfigProvider) {
            1 * parse(_)
        }
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> [registry]
        }

        when:
        def t = new Thread({
            Thread.sleep(1000)
            Bootstrap.semaphore.release()
        })
        t.start()
        Bootstrap.main([] as String[])

        then:
        noExceptionThrown()
    }

    def 'Test start up with tagged service'() {
        given:
        def registry = Mock(IRegistryService)
        def taggedSvc = Mock(ITaggedService2) {
            1 * getTags() >> ['tag']
        }
        1 * registry.findService(IRegistry.class) >> registry
        3 * registry.findService(IEventBus.class) >> Mock(IEventBus) {
            1 * fire(_ as SystemStartingUpEvent)
            1 * fire(_ as SystemShuttingDownEvent, true)
        }
        registry.findService(CliConfigProvider.class) >> Mock(CliConfigProvider) {
            1 * parse(_)
        }
        SystemBootstrap.appSvcLoader = Mock(AppServiceLoader) {
            loadServices() >> [registry, taggedSvc]
        }

        when:
        def t = new Thread({
            Thread.sleep(1000)
            Bootstrap.semaphore.release()
        })
        t.start()
        Bootstrap.main([] as String[])

        then:
        noExceptionThrown()
    }

    interface IRegistryService extends IRegistry, IService {}

    interface ITaggedService2 extends IService, ITagged {}
}
