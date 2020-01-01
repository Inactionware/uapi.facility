this._reqPerms = new uapi.auth.IPermission[] {
<#list authenticates as authenticate>
                new uapi.auth.Permission("${authenticate.resourceId()}", ${authenticate.requiredActions()})<#sep>, </#sep>
</#list>
            };