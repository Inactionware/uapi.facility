package uapi.web.servlet;

import uapi.behavior.IExecutionContext;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.service.annotation.Service;
import uapi.web.http.HttpRequestEvent;

/**
 * Created by min on 2017/5/29.
 */
@Service
@Action
public abstract class ExtractRequest {

    @ActionDo
    public void extract(
            final HttpRequestEvent event, final IExecutionContext context) {

    }
}
