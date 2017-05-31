package uapi.web.http;

import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;

/**
 * Created by xquan on 5/31/2017.
 */
@Service
public class RequestDataRegistry {

    @Inject
    protected IRequestDataMeta _reqDataMeta;
}
