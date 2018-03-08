package uapi.net.telnet.internal

import spock.lang.Ignore
import spock.lang.Specification

class TelnetListenerTest extends Specification {

    @Ignore
    def 'Test telnet server'() {
        when:
        def telnetListener = new TelnetListener()
        telnetListener._port = 10023
        telnetListener.startUp()

        then:
        noExceptionThrown()
    }
}
