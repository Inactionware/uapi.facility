package uapi.web;

/**
 * The validator checks the data field is provided or not
 */
public class RequiredValidator implements IValidator {

    @Override
    public void validate(
            final String name,
            final String value
    ) throws WebException {
        if (value == null) {
            throw WebException.builder()
                    .errorCode(WebErrors.PARAM_IS_REQUIRED)
                    .variables(new WebErrors.ParamIsRequired()
                            .name(name))
                    .build();
        }
    }
}
