package uapi.net.internal;

import uapi.GeneralException;
import uapi.codegen.ClassMeta;
import uapi.codegen.IBuilderContext;
import uapi.net.INetListener;
import uapi.net.annotation.NetListener;
import uapi.rx.Looper;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

public class NetListenerParser {

    public static final String makeInitializerClassName(String listenerClassName) {
        return listenerClassName + "_Initializer_Generated";
    }

    public static final String makeMetaClassName(String listenerClassName) {
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

            String listenerClassName = classElement.getSimpleName().toString();
            ClassMeta.Builder listenerClassBuilder = builderCtx.findClassBuilder(classElement);

            String initializerClassName = makeInitializerClassName(listenerClassName);
            ClassMeta.Builder initializerClassBuilder = builderCtx.newClassBuilder(pkgName, initializerClassName);

            String metaClassName = makeInitializerClassName(listenerClassName);
            ClassMeta.Builder listenerMetaClsBuilder = builderCtx.newClassBuilder(pkgName, metaClassName);

            ListenerModel model = new ListenerModel(type, listenerClassName, initializerClassName);
            listenerClassBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
            initializerClassBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
            listenerMetaClsBuilder.putTransience(NetListenerHandler.MODEL_NAME, model);
        });
    }
}
