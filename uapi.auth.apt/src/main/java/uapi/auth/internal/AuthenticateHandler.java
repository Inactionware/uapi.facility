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
import uapi.auth.IPermission;
import uapi.auth.PermissionVerifier;
import uapi.auth.annotation.Authenticate;
import uapi.auth.annotation.Authenticates;
import uapi.behavior.*;
import uapi.behavior.annotation.Action;
import uapi.codegen.*;
import uapi.common.StringHelper;
import uapi.resource.IResourceTypeManager;
import uapi.rx.Looper;
import uapi.service.IInjectableHandlerHelper;
import uapi.service.IServiceHandlerHelper;
import uapi.service.QualifiedServiceId;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(IAnnotationsHandler.class)
public class AuthenticateHandler extends AnnotationsHandler {

    private static final String TEMP_BY                 = "template/by_method.ftl";
    private static final String TEMPLATE_INPUT_METAS    = "template/inputMetas_method.ftl";
    private static final String TEMP_PROCESS            = "template/interceptor_process_method.ftl";
    private static final String TEMP_INTERC_CONSTR      = "template/interceptor_constructor.ftl";

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { Authenticates.class, Authenticate.class };

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
        if (annotationType != Authenticates.class && annotationType != Authenticate.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(classElement -> {
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", classElement.getSimpleName().toString());
            }
            builderContext.checkAnnotations(classElement, Action.class);

            Authenticate[] authenticates;
            if (classElement.getAnnotation(Authenticates.class) == null) {
                authenticates = new Authenticate[] { classElement.getAnnotation(Authenticate.class) };
            } else {
                authenticates = classElement.getAnnotation(Authenticates.class).value();
            }
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
        classBuilder.addImplement(IIntercepted.class)
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("by")
                        .setReturnTypeName(StringHelper.makeString("{}[]", ActionIdentify.class.getTypeName()))
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

        final String fieldReqPerms = "_reqPerms";
        final String fieldResTypeMgr = "_resTypeMgr";
        Map<String, Object> model = new HashMap<>();
        model.put("authenticates", anthenticates);
        model.put("actionParameterMetas", actionMethodMeta.parameterMetas());
        Template tempInputMetas = builderContext.loadTemplate(TEMPLATE_INPUT_METAS);
        Template tempProc = builderContext.loadTemplate(TEMP_PROCESS);
        Template tempConstructor = builderContext.loadTemplate(TEMP_INTERC_CONSTR);

        ClassMeta.Builder classBuilder = builderContext.newClassBuilder(pkgName, clsName);
        IServiceHandlerHelper svcHelper = builderContext.getHelper(IServiceHandlerHelper.name);
        IInjectableHandlerHelper injectHelper = builderContext.getHelper(IInjectableHandlerHelper.name);
        injectHelper.addDependency(
                builderContext,
                classBuilder,
                fieldResTypeMgr,
                IResourceTypeManager.class.getCanonicalName(),
                IResourceTypeManager.class.getCanonicalName(),
                QualifiedServiceId.FROM_LOCAL,
                false, false,
                null,
                false
        );
        svcHelper.becomeService(builderContext, classBuilder, IAction.class.getCanonicalName());
        classBuilder
                .addImplement(IInterceptor.class.getCanonicalName())
                .setParentClassName(PermissionVerifier.class.getName())
                .addFieldBuilder(FieldMeta.builder()
                        .addModifier(Modifier.PRIVATE, Modifier.FINAL)
                        .setName(fieldReqPerms)
                        .setTypeName(Type.toArrayType(IPermission.class)))
                .addMethodBuilder(MethodMeta.builder()
                        .setName(clsName)
                        .addCodeBuilder(CodeMeta.builder()
                                .setTemplate(tempConstructor)
                                .setModel(model)))
                .addMethodBuilder(MethodMeta.builder()
                        .setName("inputMetas")
                        .addModifier(Modifier.PUBLIC)
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .setReturnTypeName(Type.toArrayType(ActionInputMeta.class))
                        .addCodeBuilder(CodeMeta.builder().setTemplate(tempInputMetas).setModel(model)))
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
                                        "return uapi.behavior.ActionIdentify.toActionId({}.class);",
                                        classBuilder.getQualifiedClassName()))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("process")
                        .setReturnTypeName(Type.VOID)
                        .addParameterBuilder(ParameterMeta.builder()
                                .setName("inputs").setType(Type.toArrayType(Object.class)))
                        .addParameterBuilder(ParameterMeta.builder()
                                .setName("outputs").setType(Type.toArrayType(ActionOutput.class)))
                        .addParameterBuilder(ParameterMeta.builder()
                                .setName("context").setType(IExecutionContext.class.getCanonicalName()))
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(tempProc)))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PROTECTED)
                        .setName("requiredPermissions")
                        .setReturnTypeName(Type.toArrayType(IPermission.class))
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return this.{};", fieldReqPerms))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PROTECTED)
                        .setName("resourceTypeManager")
                        .setReturnTypeName(IResourceTypeManager.class.getCanonicalName())
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return this.{};", fieldResTypeMgr))));
        return classBuilder.getQualifiedClassName();
    }
}
