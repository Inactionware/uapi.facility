return new uapi.web.http.IRequestDataMeta.FieldInfo[] {
<#list fieldMetas as fieldMeta>
            uapi.web.http.IRequestDataMeta.FieldInfo(
                ${fieldMeta.name},
    <#if fieldMeta.from.class.canonicalName == "uapi.web.http.IRequestDataMeta.DataFromHeader">
                new uapi.web.http.IRequestDataMeta.DataFromHeader("${fieldMeta.from.name}"),
    <#elseif fieldMeta.from.class.canonicalName == "uapi.web.http.IRequestDataMeta.DataFromParam">
                new uapi.web.http.IRequestDataMeta.DataFromParam("${fieldMeta.from.name}"),
    <#elseif fieldMeta.from.class.canonicalName == "uapi.web.http.IRequestDataMeta.DataFromUri">
                new uapi.web.http.IRequestDataMeta.DataFromUri("${fieldMeta.from.index}"),
    </#if>
                new uapi.web.IValidator[] {
    <#list fieldMeta.validators as validator>
        <#if validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.RequiredMeta">
                    new uapi.web.RequiredValidator()
        <#elseif validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.SizeMeta">
                    new uapi.web.SizeValidator(${validator.min}, ${validator.max})
        <#elseif validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.MinMeta">
            <#if validator.numberType == uapi.web.NumberValidator.NumberType.SHORT>
                    new uapi.web.MinValidator((short) ${validator.shortValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.INT>
                    new uapi.web.MinValidator((int) ${validator.intValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.LONG>
                    new uapi.web.MinValidator((long) ${validator.longValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.FLOAT>
                    new uapi.web.MinValidator((float) ${validator.floatValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.DOUBLE>
                    new uapi.web.MinValidator((double) ${validator.doubleValue})
            </#if>
         <#elseif validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.MaxMeta">
            <#if validator.numberType == uapi.web.NumberValidator.NumberType.SHORT>
                    new uapi.web.MaxValidator((short) ${validator.shortValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.INT>
                    new uapi.web.MaxValidator((int) ${validator.intValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.LONG>
                    new uapi.web.MaxValidator((long) ${validator.longValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.FLOAT>
                    new uapi.web.MaxValidator((float) ${validator.floatValue})
            <#elseif validator.numberType == uapi.web.NumberValidator.NumberType.DOUBLE>
                    new uapi.web.MaxValidator((double) ${validator.doubleValue})
            </#if>
         <#elseif validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.BoolMeta">
                    new uapi.web.BoolValidator(${validator.type})
         <#elseif validator.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.RegexpMeta">
                    new uapi.web.RegexpValidator(${validator.regexp})
        </#if>
        <#sep>, </#sep>
    </#list>
                },
    <#if fieldMeta.converter.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.PasswordConverterMeta">
                new uapi.web.PasswordConverter()
    <#elseif fieldMeta.converter.class.canonicalName == "uapi.web.http.internal.RequestDataHandler.BoolConverterMeta">
                new uapi.web.BoolConverter(${fieldMeta.converter.type})
    <#else>
                null
    </#if>
            )<#sep>, </#sep>
</#list>
            };