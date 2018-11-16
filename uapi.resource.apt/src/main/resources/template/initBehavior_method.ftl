IBehavior behavior;
<#list behaviors as behavior>
            behavior = responsibility.newBehavior("${behavior.name}", uapi.resource.ResourceRequestEvent.class, "${behavior.resource} ${behavior.name}");
            super.${behavior.name}(behavior);
            behavior.publish();
</#list>