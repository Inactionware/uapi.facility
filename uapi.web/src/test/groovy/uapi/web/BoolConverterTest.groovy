package uapi.web

import spock.lang.Specification

/**
 * Unit test for BoolConverter
 */
class BoolConverterTest extends Specification {

    def 'Test create instance'() {
        when:
        new BoolConverter(BoolType.OnOff)

        then:
        noExceptionThrown()
    }

    def 'Test convert'() {
        given:
        def converter = new BoolConverter(type)

        when:
        def result = converter.convert(input)

        then:
        result == bResult

        where:
        type                | input         | bResult
        BoolType.OnOff      | 'ON'          | true
        BoolType.OnOff      | 'On'          | true
        BoolType.OnOff      | 'nN'          | false
        BoolType.OnOff      | 'OfF'         | false
        BoolType.OnOff      | 'OF'          | false
        BoolType.TrueFalse  | 'TrUe'        | true
        BoolType.TrueFalse  | 'Ture'        | false
        BoolType.TrueFalse  | 'FAlSE'       | false
        BoolType.TrueFalse  | 'Faaa'        | false
        BoolType.YesNo      | 'YES'         | true
        BoolType.YesNo      | 'YSe'         | false
        BoolType.YesNo      | 'NO'          | false
        BoolType.YesNo      | 'ON'          | false
        BoolType.YesNo      | 'True'        | false
    }
}
