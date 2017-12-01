package uapi.net.internal;

import uapi.GeneralException;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IBuilderContext;
import uapi.common.ArgumentChecker;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

public class NetListenerHandler extends AnnotationsHandler {

    public static final String MODEL_NAME   = "ListenerModel";

    private final NetListenerParser _netListenerParser;
    private final AttributeParser _attributeParser;

    public NetListenerHandler() {
        this._netListenerParser = new NetListenerParser();
        this._attributeParser = new AttributeParser();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return new Class[] {
                NetListener.class, Attribute.class
        };
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        ArgumentChecker.required(annotationType, "annotationType");

        if (annotationType.equals(NetListener.class)) {
            this._netListenerParser.parse(builderContext, elements);
        } else if (annotationType.equals(Attribute.class)) {
            this._attributeParser.parse(builderContext, elements);
        } else {
            throw new GeneralException("Unsupported annotation - {}", annotationType.getName());
        }
    }
}
