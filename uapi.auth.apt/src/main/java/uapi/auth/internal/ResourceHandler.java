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
import uapi.auth.IResource;
import uapi.auth.IResourceLoader;
import uapi.auth.IResourceType;
import uapi.auth.annotation.Resource;
import uapi.codegen.*;
import uapi.common.StringHelper;
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

    private static final String TEMP_GET_IDS            = "template/getIds_method.ftl";
    private static final String TEMP_SET_LOADER         = "template/setLoader_method.ftl";
    private static final String TEMP_FIND_RESOURCE      = "template/findResource_method.ftl";

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
            Resource res = classElement.getAnnotation(Resource.class);
            String pkgName = builderContext.packageName(classElement);
            String clsName = classElement.getSimpleName().toString();
            String resName = res.name();
            int availableActions = res.availableActions();

            ClassMeta.Builder classBudr = builderContext.newClassBuilder(
                    pkgName, StringHelper.makeString("Resource_{}_Generated", clsName));
            implementIService(classBudr, builderContext);
            implementIResourceType(classBudr, builderContext, res);
        });
    }

    private void implementIService(
            final ClassMeta.Builder classBuilder,
            final IBuilderContext builderCtx
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put(VAR_SVC_IDS, new String[] { IResourceType.class.getCanonicalName() });

        Template tempGetIds = builderCtx.loadTemplate(TEMP_GET_IDS);

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
        final String KEY_FIELD_LOADER   = "FieldLoader";
        final String VAL_FIELD_LOADER   = "_loader";
        Map<String, Object> model = new HashMap<>();
        model.put(KEY_FIELD_LOADER, VAL_FIELD_LOADER);

        Template tempSetLoader = builderCtx.loadTemplate(TEMP_SET_LOADER);
        Template tempFindResource = builderCtx.loadTemplate(TEMP_FIND_RESOURCE);

        classBuilder.addImplement(IResourceType.class)
                .addFieldBuilder(FieldMeta.builder()
                        .addModifier(Modifier.PRIVATE)
                        .setTypeName(IResourceLoader.class.getCanonicalName())
                        .setName(VAL_FIELD_LOADER))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("name")
                        .setReturnTypeName(Type.Q_STRING)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return \"{}\";", resource.name()))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("availableActions")
                        .setReturnTypeName(Type.INTEGER)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(StringHelper.makeString("return {};", resource.availableActions()))))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("setLoader")
                        .addParameterBuilder(ParameterMeta.builder()
                                .addModifier(Modifier.FINAL)
                                .setType(IResourceLoader.class.getCanonicalName())
                                .setName("loader"))
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(tempSetLoader)))
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("findResource")
                        .addParameterBuilder(ParameterMeta.builder()
                                .addModifier(Modifier.FINAL)
                                .setType(IResource.class.getCanonicalName())
                                .setName("id"))
                        .addCodeBuilder(CodeMeta.builder()
                                .setModel(model)
                                .setTemplate(tempFindResource)));

    }
}
