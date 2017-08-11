package uapi.app.web.servlet.internal;

import uapi.Tags;
import uapi.config.IConfigProvider;
import uapi.config.IConfigTracer;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;

import javax.servlet.ServletContext;

/**
 * he config provider to parse servlet context parameter configuration
 */
@Service
@Tag(Tags.CONFIG)
public class ContextConfigProvider implements IConfigProvider {

    @Inject
    protected IConfigTracer _configTracer;

    public void parse(ServletContext servletContext) {
        Looper.on(servletContext.getInitParameterNames())
                .foreach(name -> this._configTracer.onChange(
                        QUALIFY_SYSTEM + name, servletContext.getInitParameter(name)));
    }
}
