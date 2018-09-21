/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.internal;

import uapi.GeneralException;
import uapi.auth.annotation.Authenticate;
import uapi.auth.annotation.Authenticates;
import uapi.behavior.IActionHandlerHelper;
import uapi.behavior.annotation.Action;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IBuilderContext;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.Set;

public class AuthenticateHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { Authenticates.class };

    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return orderedAnnotations;
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        if (annotationType != Authenticate.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(classElement -> {
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", classElement.getSimpleName().toString());
            }
            builderContext.checkAnnotations(classElement, Action.class);

            Authenticate[] authenticates = classElement.getAnnotation(Authenticates.class).value();
            // Find out input type and output type from the action
            IActionHandlerHelper actionHelper = builderContext.getHelper(IActionHandlerHelper.name);
            IActionHandlerHelper.ActionMethodMeta actionMethodMeta = actionHelper.parseActionMethod(classElement);

            implementIInterceptive(builderContext, classElement);
            createInterceptor(builderContext, classElement, authenticates, actionMethodMeta);
        });
    }

    private void implementIInterceptive(
            final IBuilderContext builderContext,
            final Element classElement
    ) {

    }

    private void createInterceptor(
            final IBuilderContext builderContext,
            final Element classElement,
            final Authenticate[] anthenticates,
            final IActionHandlerHelper.ActionMethodMeta actionMethodMeta
    ) {

    }
}
