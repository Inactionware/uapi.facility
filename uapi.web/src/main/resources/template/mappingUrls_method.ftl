return new java.lang.String[] {
<#list mappingUris as mappingUri>
            ${mappingUri}<#sep>, </#sep>
</#list>
        };