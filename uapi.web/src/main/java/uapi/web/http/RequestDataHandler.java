package uapi.web.http;

import com.google.auto.service.AutoService;
import uapi.GeneralException;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IAnnotationsHandler;
import uapi.codegen.IBuilderContext;
import uapi.rx.Looper;
import uapi.web.http.annotation.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * The handler process RequestData annotation, and it also handle FromHeader, FromParam,
 * FromUri annotation.
 */
@AutoService(IAnnotationsHandler.class)
public class RequestDataHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return new Class[] { RequestData.class };
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        if (annotationType != RequestData.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(classElement -> {
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", classElement.getSimpleName().toString());
            }
            List<? extends Element> childElements = classElement.getEnclosedElements();
            Looper.on(childElements)
                    .filter(element -> element.getKind() == ElementKind.METHOD)
                    .next(element -> {
                        if (element.getAnnotation(FromHeader.class) != null) {
                            handleFromHeader(builderContext, element);
                        } else if (element.getAnnotation(FromUri.class) != null) {
                            handleFromUri(builderContext, element);
                        } else if (element.getAnnotation(FromParam.class) != null) {
                            handleFromParam(builderContext, element);
                        }
                    }).foreach(element -> {
                        if (hasAnnotation(element, Required.class)) {
                            handleRequired(builderContext, element);
                        }
                        if (hasAnnotation(element, Size.class)) {
                            handeSize(builderContext, element);
                        }
                        if (hasAnnotation(element, Bool.class)) {
                            handleBool(builderContext, element);
                        }
                        if (hasAnnotation(element, Min.class)) {
                            handleMin(builderContext, element);
                        }
                        if (hasAnnotation(element, Max.class)) {
                            handleMax(builderContext, element);
                        }
                        if (hasAnnotation(element, Regexp.class)) {
                            handleRegexp(builderContext, element);
                        }
                        if (hasAnnotation(element, Password.class)) {
                            handlePassword(builderContext, element);
                        }
                    });

            // Todo: make class builder
        });
    }

    private boolean hasAnnotation(
            final Element element,
            final Class<? extends Annotation> annotationType) {
        if (element.getAnnotation(annotationType) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void handleFromHeader(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleFromParam(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleFromUri(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleRequired(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handeSize(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleBool(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleMin(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleMax(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handleRegexp(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }

    private void handlePassword(
            final IBuilderContext builderContext,
            final Element element
    ) {

    }
}
