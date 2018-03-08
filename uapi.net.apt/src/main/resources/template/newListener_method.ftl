// Verify all required attribute are set
<#list attributes as attribute>
    <#if attribute.isRequired()>
        if (! this._attributes.containsKey("${attribute.name}")) {
            throw new uapi.GeneralException("Missing required attribute - ${attribute.name}");
        }
    </#if>
</#list>

        ${listenerClassName} listener = new ${listenerClassName}();
        for (java.util.Map.Entry<String, Object> attrEntry : this._attributes.entrySet()) {
<#list attributes as attribute>
            if (attrEntry.getKey().equals("${attribute.name}")) {
                listener.${attribute.setterName}((${attribute.type}) attrEntry.getValue());
                continue;
            }
</#list>
            throw new uapi.GeneralException("Unsupported attribute - {} on listener type - {}", attrEntry.getKey(), "${listenerType}");
        }
        return listener;