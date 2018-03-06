package uapi.net.internal;

import uapi.GeneralException;
import uapi.codegen.ClassMeta;
import uapi.codegen.IBuilderContext;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
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
            builderCtx.checkModifiers(classElement, NetListener.class, Modifier.PRIVATE, Modifier.STATIC, Modifier.PRIVATE);
            String pkgName = builderCtx.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
            String listenerClassName = classElement.getSimpleName().toString();
            ClassMeta.Builder listenerClassBuilder = builderCtx.findClassBuilder(pkgName, listenerClassName, false);
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

            Attribute attr = fieldElement.getAnnotation(Attribute.class);
            AttributeModel attrModel = new AttributeModel();
            attrModel.setName(attr.value());
            attrModel.setRequired(attr.isRequired());
            attrModel.setFieldName(fieldElement.getSimpleName().toString());
            attrModel.setFieldType(fieldElement.asType().toString());
            model.addAttribute(attrModel);
        });
    }
}
