package uapi.app.web.servlet.internal;

import uapi.Tags;
import uapi.config.IConfigProvider;
import uapi.config.IConfigTracer;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;

import javax.servlet.FilterConfig;

/**
 * The config provider to parse filter configuration
 */
@Service
@Tag(Tags.CONFIG)
public class FilterConfigProvider implements IConfigProvider {

    @Inject
    protected IConfigTracer _configTracer;

    public void parse(FilterConfig filterCfg) {
        if (filterCfg == null) {
            return;
        }
        Looper.on(filterCfg.getInitParameterNames())
                .foreach(name -> this._configTracer.onChange(
                        QUALIFY_SYSTEM + name, filterCfg.getInitParameter(name)));
    }
}
