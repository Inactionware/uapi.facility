package uapi.web;

/**
 * The validator checks the string request data is valid or not
 */
public class RegexpValidator implements IValidator {

    private final String _regexp;

    public RegexpValidator(String regexp) {
        this._regexp = regexp;
    }

    @Override
    public void validate(String name, String value) throws WebException {
        if (! value.matches(this._regexp)) {
            throw WebException.builder()
                    .errorCode(WebErrors.UNMATCHED_VALUE_BY_REGEXP)
                    .variables(new WebErrors.UnmatchedValueByRegexp()
                            .name(name)
                            .value(value)
                            .regexp(this._regexp))
                    .build();
        }
    }
}
