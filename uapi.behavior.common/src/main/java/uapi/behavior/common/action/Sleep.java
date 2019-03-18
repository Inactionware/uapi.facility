package uapi.behavior.common.action;

import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.common.IntervalTime;
import uapi.service.Tags;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;

@Service
@Action
@Tag(Tags.BEHAVIOR)
public class Sleep {

    @ActionDo
    public void execute(
            final IntervalTime time
    ) throws InterruptedException {
        Thread.sleep(time.milliseconds());
    }
}
