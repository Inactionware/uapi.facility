package uapi.net.internal;

import uapi.GeneralException;
import uapi.codegen.ClassMeta;
import uapi.codegen.IBuilderContext;
import uapi.net.annotation.NetListener;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.Set;

public class NetListenerParser {

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

            String listenerClassName = classElement.getSimpleName().toString();
            ClassMeta.Builder netListenerClassBuilder = builderCtx.findClassBuilder(classElement);

            String initializerClassName = listenerClassName + "_Initializer_Generated";
            ClassMeta.Builder metaClassName = builderCtx.newClassBuilder(pkgName, initializerClassName);

            ClassMeta.Builder netListenerMetaClsBuilder = builderCtx.newClassBuilder(pkgName, listenerClassName + "_Meta_Generated");

            ListenerModel model = new ListenerModel(type, listenerClassName, initializerClassName);
            netListenerClassBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
            metaClassName.putTransience(NetListenerHandler.MODEL_NAME, model);
            netListenerMetaClsBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
        });
    }
}
