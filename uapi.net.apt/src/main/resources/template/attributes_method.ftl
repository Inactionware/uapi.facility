return new uapi.net.NetListenerAttribute[] {
<#list attributes as attribute>
            new uapi.net.NetListenerAttribute("${attribute.name}", ${attribute.isRequired()?c})<#sep>,</#sep>
</#list>
        };