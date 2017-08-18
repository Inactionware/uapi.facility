package uapi.app.web.servlet.internal;

import uapi.Tags;
import uapi.common.StringHelper;
import uapi.config.IConfigProvider;
import uapi.config.IConfigTracer;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * he config provider to parse servlet context parameter configuration
 */
@Service
@Tag(Tags.CONFIG)
public class ContextConfigProvider implements IConfigProvider {

    private static final String VAR_APP_DIR = "APP_DIR";

    @Inject
    protected IConfigTracer _configTracer;

    public void parse(final ServletContext servletContext) {
        Looper.on(servletContext.getInitParameterNames())
                .foreach(name -> this._configTracer.onChange(
                        QUALIFY_SYSTEM + name,
                        replaceVariables(servletContext, servletContext.getInitParameter(name))));
    }

    private String replaceVariables(
            final ServletContext serveltCtx,
            final String configValue
    ) {
        if (StringHelper.isNullOrEmpty(configValue)) {
            return configValue;
        }
        Map<String, String> varMap = new HashMap<>();
        varMap.put(VAR_APP_DIR, serveltCtx.getRealPath("/"));
        return StringHelper.makeString(configValue, varMap);
    }
}
