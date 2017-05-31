package uapi.web.http;

import uapi.GeneralException;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IBuilderContext;
import uapi.web.http.annotation.FromHeader;
import uapi.web.http.annotation.FromParam;
import uapi.web.http.annotation.RequestData;
import uapi.web.http.annotation.FromUri;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by xquan on 5/31/2017.
 */
public class RequestDataHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return new Class[] {
                RequestData.class, FromHeader.class, FromParam.class, FromUri.class
        };
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext iBuilderContext,
            final Class<? extends Annotation> aClass,
            final Set<? extends Element> set
    ) throws GeneralException {

    }
}
