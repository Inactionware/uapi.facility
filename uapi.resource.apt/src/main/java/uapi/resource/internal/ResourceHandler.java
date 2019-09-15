/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource.internal;

import com.google.auto.service.AutoService;
import freemarker.template.Template;
import uapi.GeneralException;
import uapi.Type;
import uapi.codegen.*;
import uapi.common.StringHelper;
import uapi.resource.IResourceType;
import uapi.resource.annotation.Resource;
import uapi.rx.Looper;
import uapi.service.IService;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(IAnnotationsHandler.class)
public class ResourceHandler extends AnnotationsHandler {

    private static final String TEMP_GET_IDS            = "template/resource_getIds_method.ftl";

    private static final String VAR_SVC_IDS = "serviceIds";

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { Resource.class };

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
        if (annotationType != Resource.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(classElement -> {
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", classElement.getSimpleName().toString());
            }
            var res = classElement.getAnnotation(Resource.class);
            var pkgName = builderContext.packageName(classElement);
            var clsName = classElement.getSimpleName().toString();
            var resName = res.type();
            var availableActions = res.availableActions();

            var classBudr = builderContext.newClassBuilder(
                    pkgName, StringHelper.makeString("Resource_{}_Generated", clsName));
            implementIService(classBudr, builderContext);
            implementIResourceType(classBudr, builderContext, res);
        });
    }

    private void implementIService(
            final ClassMeta.Builder classBuilder,
            final IBuilderContext builderCtx
    ) {
        var model = new HashMap<String, Object>();
        model.put(VAR_SVC_IDS, new String[] { IResourceType.class.getCanonicalName() });

        var tempGetIds = builderCtx.loadTemplate(TEMP_GET_IDS);

        classBuilder.addImplement(IService.class)
                .addAnnotationBuilder(AnnotationMeta.builder()
                        .setName(AutoService.class.getCanonicalName())
                        .addArgument(ArgumentMeta.builder()
                                .setName("value")
                                .setIsString(false)
                                .setValue(IService.class.getCanonicalName() + ".class")))
                .addImplement(IService.class.getCanonicalName())
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .setName(IService.METHOD_AUTOACTIVE)
                        .addModifier(Modifier.PUBLIC)
                        .setReturnTypeName(IService.METHOD_AUTOACTIVE_RETURN_TYPE)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return {};", false))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .setName(IService.METHOD_GETIDS)
                        .addModifier(Modifier.PUBLIC)
                        .setReturnTypeName(IService.METHOD_GETIDS_RETURN_TYPE)
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(tempGetIds)));
    }

    private void implementIResourceType(
            final ClassMeta.Builder classBuilder,
            final IBuilderContext builderCtx,
            final Resource resource
    ) {
        final var KEY_FIELD_LOADER   = "FieldLoader";
        final var VAL_FIELD_LOADER   = "_loader";
        var model = new HashMap<String, Object>();
        model.put(KEY_FIELD_LOADER, VAL_FIELD_LOADER);

        classBuilder.setParentClassName(ResourceType.class.getCanonicalName())
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("name")
                        .setReturnTypeName(Type.Q_STRING)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return \"{}\";", resource.type()))));
    }
}
