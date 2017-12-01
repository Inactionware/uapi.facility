package uapi.net.internal;

import uapi.GeneralException;
import uapi.codegen.IBuilderContext;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Set;

public class AttributeParser {

    public void parse(
            final IBuilderContext builderCtx,
            final Set<? extends Element> elements
    ) {
        Looper.on(elements).foreach(fieldElement -> {
            if (fieldElement.getKind() != ElementKind.FIELD) {
                throw new GeneralException(
                        "The element {} must be a field element", fieldElement.getSimpleName().toString()
                );
            }
        });
    }
}
