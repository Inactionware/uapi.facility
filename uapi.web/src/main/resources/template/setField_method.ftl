<#list fieldMetas as field>
if ("${field.name}".equals(field)) {
            this.${field.name} = (${field.type}) value;
            return;
        }
        throw new uapi.GeneralException("Unsupported set field - {} on class - {}", field, super.getClass().getName());
</#list>
