/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.internal;

import com.google.auto.service.AutoService;
import freemarker.template.Template;
import uapi.GeneralException;
import uapi.Type;
import uapi.auth.PermissionVerifier;
import uapi.auth.annotation.Authenticate;
import uapi.auth.annotation.Authenticates;
import uapi.behavior.*;
import uapi.behavior.annotation.Action;
import uapi.codegen.*;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(IAnnotationsHandler.class)
public class AuthenticateHandler extends AnnotationsHandler {

    private static final String TEMP_BY         = "template/by_method.ftl";
    private static final String TEMP_PROCESS    = "template/process_method.ftl";

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

            String interceptorClass = createInterceptor(builderContext, classElement, authenticates, actionMethodMeta);
            implementIInterceptive(builderContext, classElement, interceptorClass);

        });
    }

    private void implementIInterceptive(
            final IBuilderContext builderContext,
            final Element classElement,
            final String interceptorClass
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put("interceptorClass", interceptorClass);
        Template temp = builderContext.loadTemplate(TEMP_BY);

        ClassMeta.Builder classBuilder = builderContext.findClassBuilder(classElement);
        classBuilder.addImplement(IInterceptive.class)
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("by")
                        .setReturnTypeName(ActionIdentify.class.getTypeName())
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(temp)));
    }

    private String createInterceptor(
            final IBuilderContext builderContext,
            final Element classElement,
            final Authenticate[] anthenticates,
            final IActionHandlerHelper.ActionMethodMeta actionMethodMeta
    ) {
        String pkgName = builderContext.packageName(classElement);
        String clsName = "Interceptor_" + classElement.getSimpleName().toString() + "_Generated";
        String ioType = actionMethodMeta.inputType();

        Map<String, Object> model = new HashMap<>();
        Template temp = builderContext.loadTemplate(TEMP_PROCESS);

        ClassMeta.Builder classBuilder = builderContext.newClassBuilder(pkgName, clsName);
        classBuilder
                .addAnnotationBuilder(AnnotationMeta.builder()
                        .setName(AutoService.class.getCanonicalName())
                        .addArgument(ArgumentMeta.builder()
                                .setName("value")
                                .setValue(IAction.class.getCanonicalName() + ".class")
                                .setIsString(false)))
                .addImplement(StringHelper.makeMD5("{}<{}>", IInterceptor.class.getName(), ioType))
                .setClassName(PermissionVerifier.class.getName())
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("inputType")
                        .setReturnTypeName(ioType)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return {}.class;", ioType))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("isAnonymous")
                        .setReturnTypeName(Type.BOOLEAN)
                        .addCodeBuilder(CodeMeta.builder().addRawCode("return false;")))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("getId")
                        .setReturnTypeName(ActionIdentify.class.getCanonicalName())
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString(
                                        "return uapi.behavior.ActionIdentify.toActionId({});",
                                        classBuilder.getQualifiedClassName()))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("process")
                        .setReturnTypeName(ioType)
                        .addParameterBuilder(ParameterMeta.builder()
                                .addModifier(Modifier.FINAL)
                                .setName("input")
                                .setType(ioType))
                        .addParameterBuilder(ParameterMeta.builder()
                                .addModifier(Modifier.PUBLIC)
                                .setName("context")
                                .setType(IExecutionContext.class.getCanonicalName()))
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(temp)));
        return classBuilder.getQualifiedClassName();
    }
}
