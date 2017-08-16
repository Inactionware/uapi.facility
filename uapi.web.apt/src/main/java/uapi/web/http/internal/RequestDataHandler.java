package uapi.web.http.internal;

import com.google.auto.service.AutoService;
import freemarker.template.Template;
import uapi.GeneralException;
import uapi.Type;
import uapi.codegen.*;
import uapi.rx.Looper;
import uapi.service.annotation.Service;
import uapi.web.BoolType;
import uapi.web.NumberValidator;
import uapi.web.http.IRequestData;
import uapi.web.http.IRequestDataMeta;
import uapi.web.http.annotation.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The handler process RequestData annotation, and it also handle FromHeader, FromParam,
 * FromUri annotation.
 */
@AutoService(IAnnotationsHandler.class)
public class RequestDataHandler extends AnnotationsHandler {

    private static final String TEMP_SET_FIELD      = "template/setField_method.ftl";
    private static final String TEMP_MAPPING_URIS   = "template/mappingUrls_method.ftl";
    private static final String TEMP_FIELD_INFOS    = "template/fieldInfos_method.ftl";
    private static final String TEMP_NEW_INSTANCE   = "template/newInstance_method.ftl";

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return new Class[] { RequestData.class };
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        if (annotationType != RequestData.class) {
            throw new GeneralException("Unsupported annotation type - {}", annotationType.getCanonicalName());
        }

        Looper.on(elements).foreach(classElement -> {
            String dataType = classElement.asType().toString();
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new GeneralException(
                        "The element {} must be a class element", dataType);
            }
            MappingUris annoMappingUris = classElement.getAnnotation(MappingUris.class);
            if (annoMappingUris == null) {
                throw new GeneralException(
                        "A MappingUris must be defined for element - {}", dataType);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("mappingUris", annoMappingUris.value());
            model.put("dataType", dataType);
            List<FieldMeta> fieldMetas = new ArrayList<>();
            model.put("fieldMetas", fieldMetas);

            List<? extends Element> childElements = classElement.getEnclosedElements();
            Looper.on(childElements)
                    .filter(element -> element.getKind() == ElementKind.FIELD)
                    .filter(element -> {
                        FieldMeta fieldMeta = new FieldMeta();
                        fieldMeta.name = element.getSimpleName().toString();
                        fieldMeta.type = element.asType().toString();
                        if (element.getAnnotation(FromHeader.class) != null) {
                            fieldMetas.add(fieldMeta);
                            handleFromHeader(element, fieldMetas);
                            return true;
                        } else if (element.getAnnotation(FromUri.class) != null) {
                            fieldMetas.add(fieldMeta);
                            handleFromUri(element, fieldMetas);
                            return true;
                        } else if (element.getAnnotation(FromParam.class) != null) {
                            fieldMetas.add(fieldMeta);
                            handleFromParam(element, fieldMetas);
                            return true;
                        }
                        return false;
                    }).foreach(element -> {
                        String fieldName = element.getSimpleName().toString();
                        FieldMeta fieldMeta = Looper.on(fieldMetas).filter(meta -> meta.name.equals(fieldName)).first();
                        if (hasAnnotation(element, Required.class)) {
                            handleRequired(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Size.class)) {
                            handleSize(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Bool.class)) {
                            handleBool(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Min.class)) {
                            handleMin(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Max.class)) {
                            handleMax(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Regexp.class)) {
                            handleRegexp(element, fieldMeta);
                        }
                        if (hasAnnotation(element, Password.class)) {
                            handlePassword(element, fieldMeta);
                        }
                    });

            // Make data class builder
            Template setFieldTemp = builderContext.loadTemplate(TEMP_SET_FIELD);

            ClassMeta.Builder dataClsBuilder = builderContext.findClassBuilder(classElement);
            String reqDataType = dataClsBuilder.getGeneratedClassName();
            model.put("dataType", reqDataType);
            dataClsBuilder.addImplement(IRequestData.class.getCanonicalName())
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder()
                                    .setName(AnnotationMeta.OVERRIDE))
                            .addModifier(Modifier.PUBLIC)
                            .setName("setField")
                            .setReturnTypeName(Type.VOID)
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("value")
                                    .setType(Type.Q_OBJECT))
                            .addParameterBuilder(ParameterMeta.builder()
                                    .setName("field")
                                    .setType(Type.Q_STRING))
                            .addCodeBuilder(CodeMeta.builder()
                                    .setModel(model)
                                    .setTemplate(setFieldTemp)));


            // Make data meta class builder
            Template mappingUrlsTemp = builderContext.loadTemplate(TEMP_MAPPING_URIS);
            Template fieldInfosTemp = builderContext.loadTemplate(TEMP_FIELD_INFOS);
            Template newInstanceTemp = builderContext.loadTemplate(TEMP_NEW_INSTANCE);

            ClassMeta.Builder metaClassBuilder = builderContext.newClassBuilder(
                    dataClsBuilder.getPackageName(), dataClsBuilder.getClassName() + "Meta");
            metaClassBuilder.addImplement(IRequestDataMeta.class.getCanonicalName())
                    .addAnnotationBuilder(AnnotationMeta.builder()
                            .setName(Service.class.getCanonicalName())
                            .addArgument(ArgumentMeta.builder()
                                    .setName("value")
                                    .setValue(IRequestDataMeta.class.getCanonicalName() + ".class")))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder()
                                    .setName(AnnotationMeta.OVERRIDE))
                            .setName("mappingUrls")
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName(Type.STRING_ARRAY)
                            .addCodeBuilder(CodeMeta.builder()
                                    .setModel(model)
                                    .setTemplate(mappingUrlsTemp)))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder()
                                    .setName(AnnotationMeta.OVERRIDE))
                            .setName("fieldInfos")
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName("uapi.web.http.IRequestDataMeta.FieldInfo[]")
                            .addCodeBuilder(CodeMeta.builder()
                                    .setModel(model)
                                    .setTemplate(fieldInfosTemp)))
                    .addMethodBuilder(MethodMeta.builder()
                            .addAnnotationBuilder(AnnotationMeta.builder()
                                    .setName(AnnotationMeta.OVERRIDE))
                            .setName("newInstance")
                            .addModifier(Modifier.PUBLIC)
                            .setReturnTypeName(reqDataType)
                            .addCodeBuilder(CodeMeta.builder()
                                    .setModel(model)
                                    .setTemplate(newInstanceTemp)));
        });
    }

    private boolean hasAnnotation(
            final Element element,
            final Class<? extends Annotation> annotationType) {
        if (element.getAnnotation(annotationType) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void handleFromHeader(
            final Element element,
            final List<FieldMeta> fieldMetas
    ) {
        String fieldName = element.getSimpleName().toString();
        FieldMeta fieldMeta = Looper.on(fieldMetas).filter(meta -> meta.name.equals(fieldName)).first();
        IRequestDataMeta.DataFromHeader from = new IRequestDataMeta.DataFromHeader(
                element.getAnnotation(FromHeader.class).value());
        fieldMeta.from = from;
    }

    private void handleFromParam(
            final Element element,
            final List<FieldMeta> fieldMetas
    ) {
        String fieldName = element.getSimpleName().toString();
        FieldMeta fieldMeta = Looper.on(fieldMetas).filter(meta -> meta.name.equals(fieldName)).first();
        IRequestDataMeta.DataFromParam from = new IRequestDataMeta.DataFromParam(
                element.getAnnotation(FromParam.class).value());
        fieldMeta.from = from;
    }

    private void handleFromUri(
            final Element element,
            final List<FieldMeta> fieldMetas
    ) {
        String fieldName = element.getSimpleName().toString();
        FieldMeta fieldMeta = Looper.on(fieldMetas).filter(meta -> meta.name.equals(fieldName)).first();
        IRequestDataMeta.DataFromUri from = new IRequestDataMeta.DataFromUri(
                element.getAnnotation(FromUri.class).value());
        fieldMeta.from = from;
    }

    private void handleRequired(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        fieldMeta.validators.add(new RequiredMeta());
    }

    private void handleSize(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        SizeMeta sizeMeta = new SizeMeta();
        Size annSize = element.getAnnotation(Size.class);
        sizeMeta.min = annSize.min();
        sizeMeta.max = annSize.max();
        fieldMeta.validators.add(sizeMeta);
    }

    private void handleBool(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        BoolMeta boolMeta = new BoolMeta();
        Bool annBool = element.getAnnotation(Bool.class);
        boolMeta.type = annBool.value();
        fieldMeta.validators.add(boolMeta);
        if (fieldMeta.converter != null) {
            throw new GeneralException("Specified multiple converter - {} to field - {}",
                    fieldMeta.converter.getClass().getName(), element.getSimpleName().toString());
        }
        BoolConverterMeta boolConverterMeta = new BoolConverterMeta();
        boolConverterMeta.type = annBool.value();
        fieldMeta.converter = boolConverterMeta;
    }

    private void handleMin(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        MinMeta minMeta = new MinMeta();
        Min annMin = element.getAnnotation(Min.class);
        String fieldType = element.asType().toString();
        if (Type.SHORT.equals(fieldType) || Type.Q_SHORT.equals(fieldType)) {
            minMeta.numberType = NumberValidator.NumberType.SHORT;
            minMeta.shortValue = annMin.shortValue();
        } else if (Type.INTEGER.equals(fieldType) || Type.Q_INTEGER.equals(fieldType)) {
            minMeta.numberType = NumberValidator.NumberType.INT;
            minMeta.intValue = annMin.intValue();
        } else if (Type.LONG.equals(fieldType) || Type.Q_LONG.equals(fieldType)) {
            minMeta.numberType = NumberValidator.NumberType.LONG;
            minMeta.longValue = annMin.intValue();
        } else if (Type.FLOAT.equals(fieldType) || Type.Q_FLOAT.equals(fieldType)) {
            minMeta.numberType = NumberValidator.NumberType.FLOAT;
            minMeta.floatValue = annMin.floatValue();
        } else if (Type.DOUBLE.equals(fieldType) || Type.Q_DOUBLE.equals(fieldType)) {
            minMeta.numberType = NumberValidator.NumberType.DOUBLE;
            minMeta.doubleValue = annMin.doubleValue();
        } else {
            throw new GeneralException("Unsupported number type - {}", fieldType);
        }
        fieldMeta.validators.add(minMeta);
    }

    private void handleMax(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        MaxMeta maxMeta = new MaxMeta();
        Max annMax = element.getAnnotation(Max.class);
        String fieldType = element.asType().toString();
        if (Type.SHORT.equals(fieldType) || Type.Q_SHORT.equals(fieldType)) {
            maxMeta.numberType = NumberValidator.NumberType.SHORT;
            maxMeta.shortValue = annMax.shortValue();
        } else if (Type.INTEGER.equals(fieldType) || Type.Q_INTEGER.equals(fieldType)) {
            maxMeta.numberType = NumberValidator.NumberType.INT;
            maxMeta.intValue = annMax.intValue();
        } else if (Type.LONG.equals(fieldType) || Type.Q_LONG.equals(fieldType)) {
            maxMeta.numberType = NumberValidator.NumberType.LONG;
            maxMeta.longValue = annMax.intValue();
        } else if (Type.FLOAT.equals(fieldType) || Type.Q_FLOAT.equals(fieldType)) {
            maxMeta.numberType = NumberValidator.NumberType.FLOAT;
            maxMeta.floatValue = annMax.floatValue();
        } else if (Type.DOUBLE.equals(fieldType) || Type.Q_DOUBLE.equals(fieldType)) {
            maxMeta.numberType = NumberValidator.NumberType.DOUBLE;
            maxMeta.doubleValue = annMax.doubleValue();
        } else {
            throw new GeneralException("Unsupported number type - {}", fieldType);
        }
        fieldMeta.validators.add(maxMeta);
    }

    private void handleRegexp(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        RegexpMeta regexpMeta = new RegexpMeta();
        Regexp annRegexp = element.getAnnotation(Regexp.class);
        regexpMeta.regexp = annRegexp.value();
        fieldMeta.validators.add(regexpMeta);
    }

    private void handlePassword(
            final Element element,
            final FieldMeta fieldMeta
    ) {
        if (fieldMeta.converter != null) {
            throw new GeneralException("Specified multiple converter - {} to field - {}",
                    fieldMeta.converter.getClass().getName(), element.getSimpleName().toString());
        }
        fieldMeta.converter = new PasswordConverterMeta();
    }

    public static final class FieldMeta {

        public String name;

        public IRequestDataMeta.DataFrom from;

        public String type;

        public List<IValidatorMeta> validators = new ArrayList<>();

        public IConverterMeta converter;

        public String getName() {
            return this.name;
        }

        public IRequestDataMeta.DataFrom getFrom() {
            return this.from;
        }

        public String getType() {
            return this.type;
        }

        public List<IValidatorMeta> getValidators() {
            return this.validators;
        }

        public IConverterMeta getConverter() {
            return this.converter;
        }
    }

    public interface IValidatorMeta { }

    public static class RequiredMeta implements IValidatorMeta { }

    public static class SizeMeta implements IValidatorMeta {

        public int min;

        public int max;

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }
    }

    public static class MinMeta implements IValidatorMeta {

        public NumberValidator.NumberType numberType;
        public int intValue;
        public short shortValue;
        public long longValue;
        public float floatValue;
        public double doubleValue;

        public NumberValidator.NumberType getNumberType() {
            return this.numberType;
        }

        public int getIntValue() {
            return this.intValue;
        }

        public short getShortValue() {
            return this.shortValue;
        }

        public long getLongValue() {
            return this.longValue;
        }

        public float getFloatValue() {
            return this.floatValue;
        }

        public double getDoubleValue() {
            return this.doubleValue;
        }
    }

    public static class MaxMeta extends MinMeta { }

    public static class BoolMeta implements IValidatorMeta {

        public BoolType type;

        public BoolType getType() {
            return this.type;
        }
    }

    public static class RegexpMeta implements IValidatorMeta {

        public String regexp;

        public String getRegexp() {
            return this.regexp;
        }
    }

    public interface IConverterMeta { }

    public static class PasswordConverterMeta implements IConverterMeta { }

    public static class BoolConverterMeta implements IConverterMeta {

        public BoolType type;

        public BoolType getType() {
            return this.type;
        }
    }
}
