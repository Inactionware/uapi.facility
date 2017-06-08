package uapi.web

import spock.lang.Specification

/**
 * Unit test for BoolValidator
 */
class BoolValidatorTest extends Specification {

    def 'Test create instance'() {
        when:
        new BoolValidator(BoolType.YesNo)

        then:
        noExceptionThrown()
    }

    def 'Test verify failed'() {
        given:
        def validator = new BoolValidator(type)

        when:
        validator.validate('test', value)

        then:
        thrown(WebException)

        where:
        type                | value
        BoolType.YesNo      | 'es'
        BoolType.YesNo      | 'yes '
        BoolType.YesNo      | 'true'
        BoolType.YesNo      | 'on'
        BoolType.TrueFalse  | 'yes'
        BoolType.OnOff      | 'false'
        BoolType.OnOff      | 'Of'
    }

    def 'Test verify success'() {
        given:
        def validator = new BoolValidator(type)

        when:
        validator.validate('test', value)

        then:
        noExceptionThrown()

        where:
        type                | value
        BoolType.OnOff      | 'ON'
        BoolType.OnOff      | 'OfF'
        BoolType.YesNo      | 'YEs'
        BoolType.YesNo      | 'NO'
        BoolType.TrueFalse  | 'TrUE'
        BoolType.TrueFalse  | 'FalSE'
    }
}
