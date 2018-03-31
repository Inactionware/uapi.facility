package uapi.protocol;

import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Service;

import java.util.Map;

@Service
@Action
public class DecodeProtocol {

    @ActionDo
    public ResourceProcessing decode(ResourceProcessing processing) {
        return processing;
    }
}
