this._reqPerms = new uapi.auth.IPermission[] {
<#list authenticates as authenticate>
                new uapi.auth.internal.Permission("${authenticate.resourceId()}", ${authenticate.requiredActions()})<#sep>, </#sep>
</#list>
            };