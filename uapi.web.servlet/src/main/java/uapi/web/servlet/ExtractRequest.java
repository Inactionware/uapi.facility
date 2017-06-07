package uapi.web.servlet;

import uapi.behavior.IExecutionContext;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;
import uapi.web.IValidator;
import uapi.web.http.*;

/**
 * Action is used to extract request data from http request
 */
@Service
@Action
public class ExtractRequest {

    @Inject
    RequestDataMetaRegistry _dataMetaReg;

    @ActionDo
    public void extract(
            final HttpRequestEvent event, final IExecutionContext context
    ) {
        IHttpRequest request = event.request();
        IRequestDataMeta dataMeta = this._dataMetaReg.getData(request.url());
        IRequestData requestData = dataMeta.newInstance();
        Looper.on(dataMeta.fieldInfos())
                .foreach((fieldInfo -> {
                        String name = fieldInfo.name();
                        String value = fieldInfo.from().getData(request);
                        Looper.on(fieldInfo.validators()).foreach(validator -> validator.validate(name, value));
                        Object fValue = value;
                        if (fieldInfo.converter() != null) {
                            fValue = fieldInfo.converter().convert(value);
                        }
                        requestData.setField(fValue, name);
                }));
    }
}
