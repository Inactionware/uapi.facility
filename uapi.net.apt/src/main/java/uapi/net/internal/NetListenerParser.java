package uapi.net.internal;

import freemarker.template.Template;
import uapi.GeneralException;
import uapi.codegen.*;
import uapi.net.INetListener;
import uapi.net.INetListenerInitializer;
import uapi.net.INetListenerMeta;
import uapi.net.annotation.NetListener;
import uapi.rx.Looper;
import uapi.service.IServiceHandlerHelper;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

public class NetListenerParser {

    private static final String TEMP_SET_ATTRIBUTE  = "template/setAttribute_method.ftl";
    private static final String TEMP_NEW_LISTENER   = "template/newListener_method.ftl";

    private static final String TEMP_ATTRIBUTES     = "template/attributes_method.ftl";

    static String makeInitializerClassName(String listenerClassName) {
        return listenerClassName + "_Initializer_Generated";
    }

    static String makeMetaClassName(String listenerClassName) {
        return listenerClassName + "_Meta_Generated";
    }

    public void parse(
            final IBuilderContext builderCtx,
            final Set<? extends Element> elements
    ) {
        Looper.on(elements).foreach(classElement -> {
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", classElement.getSimpleName().toString()
                );
            }
            builderCtx.checkModifiers(classElement, NetListener.class, Modifier.PRIVATE, Modifier.STATIC, Modifier.PRIVATE);
            NetListener netListener = classElement.getAnnotation(NetListener.class);
            String type = netListener.type();
            String pkgName = builderCtx.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();

            // NetListener must be implemented from INetListener interface.
            TypeElement listenerType = (TypeElement) classElement;
            List<? extends TypeMirror> interfaceTypes = listenerType.getInterfaces();
            TypeMirror intfNetListener = Looper.on(interfaceTypes)
                    .filter(intfType -> intfType.toString().equals(INetListener.class.getCanonicalName()))
                    .first(null);
            if (intfNetListener == null) {
                throw new GeneralException(
                        "The listener must be implemented from {} interface - {}.{}",
                        INetListener.class.getCanonicalName(),
                        pkgName,
                        classElement.getSimpleName().toString());
            }

            // NetListener must define a non-argument constructor
            List<? extends Element> subElemnts = listenerType.getEnclosedElements();
            List<ExecutableElement> constructorElements = Looper.on(subElemnts)
                    .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                    .map(element -> (ExecutableElement) element)
                    .toList();
            if (constructorElements.size() > 0) {
                Element defaultConstructor = Looper.on(constructorElements)
                        .filter(constructorElement -> constructorElement.getParameters().size() == 0)
                        .first(null);
                if (defaultConstructor == null) {
                    throw new GeneralException(
                            "The listener must define a non-argument default constructor - {}.{}",
                            pkgName,
                            classElement.getSimpleName().toString());
                }
            }

            // Create model
            String listenerClassName = classElement.getSimpleName().toString();
            ClassMeta.Builder listenerClassBuilder = builderCtx.findClassBuilder(classElement);

            String initializerClassName = makeInitializerClassName(listenerClassName);
            ClassMeta.Builder initializerClassBuilder = builderCtx.newClassBuilder(pkgName, initializerClassName);

            String metaClassName = makeMetaClassName(listenerClassName);
            ClassMeta.Builder listenerMetaClsBuilder = builderCtx.newClassBuilder(pkgName, metaClassName);

            ListenerModel model = new ListenerModel(type, listenerClassBuilder.getGeneratedClassName(), initializerClassName);
            listenerClassBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
            initializerClassBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
            listenerMetaClsBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);

            // Create method for listener initializer class
            Template tempSetAttribute = builderCtx.loadTemplate(TEMP_SET_ATTRIBUTE);
            Template tempNewListener = builderCtx.loadTemplate(TEMP_NEW_LISTENER);
            initializerClassBuilder
                    .addImplement(INetListenerInitializer.class.getCanonicalName())
                    .addFieldBuilder(FieldMeta.builder()
                            .addModifier(Modifier.PRIVATE, Modifier.FINAL)
                            .setName("_meta")
                            .setTypeName(INetListenerMeta.class.getCanonicalName()))
                    .addFieldBuilder(FieldMeta.builder()
                            .addModifier(Modifier.PRIVATE)
                            .setName("_attributes")
                            .setIsMap(true)
                            .setTypeName(Object.class.getCanonicalName())
                            .setKeyTypeName(String.class.getCanonicalName()))
                    .addMethodBuilder(MethodMeta.builder()
                            .setName(initializerClassBuilder.getGeneratedClassName())
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("meta")
                                    .setType(INetListenerMeta.class.getCanonicalName()))
                            .addCodeBuilder(CodeMeta.builder()
                                    .addRawCode("this._meta = meta;")))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setName(INetListenerInitializer.METHOD_SET_ATTRIBUTE)
                            .setReturnTypeName(INetListenerInitializer.class.getCanonicalName())
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("name")
                                    .setType(String.class.getCanonicalName()))
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("value")
                                    .setType(Object.class.getCanonicalName()))
                            .addCodeBuilder(CodeMeta.builder()
                                    .setTemplate(tempSetAttribute)
                                    .setModel(model)))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setName(INetListenerInitializer.METHOD_NEW_LISTENER)
                            .setReturnTypeName(INetListener.class.getCanonicalName())
                            .addCodeBuilder(CodeMeta.builder()
                                    .setTemplate(tempNewListener)
                                    .setModel(model)));

            // Create method for listener meta class
            Template tempAttributes = builderCtx.loadTemplate(TEMP_ATTRIBUTES);
            listenerMetaClsBuilder
                    .addImplement(INetListenerMeta.class.getCanonicalName())
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName(String.class.getCanonicalName())
                            .setName("type")
                            .addCodeBuilder(CodeMeta.builder()
                                    .addRawCode("return \"{}\";", type)))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName("uapi.net.NetListenerAttribute[]")
                            .setName("attributes")
                            .addCodeBuilder(CodeMeta.builder()
                                    .setTemplate(tempAttributes)
                                    .setModel(model)))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName(INetListenerInitializer.class.getCanonicalName())
                            .setName("newInitializer")
                            .addCodeBuilder(CodeMeta.builder()
                                    .addRawCode("return new {}(this);", initializerClassName)));
            // The listener meta must be a Service
            IServiceHandlerHelper svcHelper = (IServiceHandlerHelper) builderCtx.getHelper(IServiceHandlerHelper.name);
            svcHelper.becomeService(builderCtx, listenerMetaClsBuilder, listenerMetaClsBuilder.getPackageName() + "." + metaClassName);
        });
    }
}
