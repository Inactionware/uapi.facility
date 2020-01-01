/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource.internal;

import freemarker.template.Template;
import uapi.GeneralException;
import uapi.Type;
import uapi.behavior.IBehaviorBuilder;
import uapi.behavior.IResponsible;
import uapi.codegen.*;
import uapi.resource.ICapable;
import uapi.resource.annotation.Behavior;
import uapi.resource.annotation.Resource;
import uapi.rx.Looper;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class BehaviorHandler extends AnnotationsHandler {

    private static final String TEMP_INIT_BEHAVIOR  = "template/initBehavior_method.ftl";
    private static final String MODEL_INIT_BEHAVIOR = "initBehavior";

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { Behavior.class };

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
        if (annotationType != Behavior.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(annotatedElement -> {
            if (annotatedElement.getKind() != ElementKind.METHOD) {
                throw new GeneralException(
                        "The element {} must be a method element", annotatedElement.getSimpleName().toString());
            }
            builderContext.checkModifiers(annotatedElement, Behavior.class, Modifier.PRIVATE, Modifier.STATIC);
            var methodElement = (ExecutableElement) annotatedElement;
            var methodName = methodElement.getSimpleName().toString();
            var returnType = methodElement.getReturnType().toString();
            if (! Type.VOID.equals(returnType)) {
                throw new GeneralException(
                        "Expect the Behavior method [{}] return void, but it return - {}",
                        methodName, returnType);
            }
            var paramElements = methodElement.getParameters();
            if (paramElements.size() != 1) {
                throw new GeneralException(
                        "Expect the Behavior method [{}] has only 1 parameter, but found - {}",
                        methodName, paramElements.size());
            }
            var paramElement = (VariableElement) paramElements.get(0);
            var paramType = paramElement.asType().toString();
            if (! IBehaviorBuilder.class.getCanonicalName().equals(paramType)) {
                throw new GeneralException(
                        "Expect the Behavior method [{}] has a parameter which type is IBehaviorBuilder, but found - {}",
                        methodName, paramType);
            }
            var classElement = methodElement.getEnclosingElement();
            builderContext.checkAnnotations(classElement, Resource.class);

            var resource = classElement.getSimpleName().toString();
            var clsBuilder = builderContext.findClassBuilder(classElement);
            var temp = builderContext.loadTemplate(this.getClass().getModule().getName(), TEMP_INIT_BEHAVIOR);
            var model = clsBuilder.createTransienceIfAbsent(MODEL_INIT_BEHAVIOR, HashMap::new);
            var behaviors = (List<BehaviorModel>) model.get("behaviors");
            if (behaviors == null) {
                behaviors = new ArrayList<>();
                model.put("behaviors", behaviors);
            }
            var bmodel = new BehaviorModel();
            bmodel.setResourceName(resource);
            bmodel.setBehaviorName(methodName);
            behaviors.add(bmodel);

            clsBuilder.addImplement(ICapable.class.getCanonicalName())
                    .addMethodBuilderIfAbsent(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .setName("initBehavior")
                            .setReturnTypeName(Type.VOID)
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("responsible")
                                    .setType(IResponsible.class.getCanonicalName()))
                            .addCodeBuilder(CodeMeta.builder()
                                    .setModel(model)
                                    .setTemplate(temp)));
        });
    }

    public static final class BehaviorModel {

        private String _resName;
        private String _behaviorName;

        public String getResourceName() {
            return this._resName;
        }

        public void setResourceName(String name) {
            this._resName = name;
        }

        public String getBehaviorName() {
            return this._behaviorName;
        }

        public void setBehaviorName(String name) {
            this._behaviorName = name;
        }
    }
}
