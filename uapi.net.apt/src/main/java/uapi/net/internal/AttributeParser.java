package uapi.net.internal;

import uapi.GeneralException;
import uapi.Type;
import uapi.codegen.*;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.HashMap;
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

            Element classElement = fieldElement.getEnclosingElement();
            builderCtx.checkModifiers(classElement, NetListener.class, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
            String pkgName = builderCtx.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
            String listenerClassName = classElement.getSimpleName().toString();
            ClassMeta.Builder listenerClassBuilder = builderCtx.findClassBuilder(classElement, false);
            if (listenerClassBuilder == null) {
                throw new GeneralException(
                        "No generated listener was found, maybe the listener was not annotated with @NetListener - {}.{}",
                        pkgName,
                        listenerClassName);
            }

            ListenerModel model = listenerClassBuilder.getTransience(NetListenerHandler.MODEL_NAME);
            if (model == null) {
                throw new GeneralException(
                        "The specific model was not found, Maybe the listener was not annotated with @NetListener - {}.{}",
                        pkgName,
                        listenerClassName);
            }

            // Create setter method
            String fieldName = fieldElement.getSimpleName().toString();
            String fieldType = fieldElement.asType().toString();
            String setterName = ClassHelper.makeSetterName(fieldName, false, false);
            listenerClassBuilder.addMethodBuilder(MethodMeta.builder()
                    .setName(setterName)
                    .addModifier(Modifier.PUBLIC)
                    .setReturnTypeName(Type.VOID)
                    .addParameterBuilder(ParameterMeta.builder()
                            .setName("value")
                            .setType(fieldType))
                    .addCodeBuilder(CodeMeta.builder()
                            .addRawCode("super.{} = value;", fieldName)));

            Attribute attr = fieldElement.getAnnotation(Attribute.class);
            AttributeModel attrModel = new AttributeModel();
            attrModel.setName(attr.value());
            attrModel.setRequired(attr.isRequired());
            attrModel.setSetterName(setterName);
            attrModel.setType(fieldType);
            model.addAttribute(attrModel);
        });
    }
}
