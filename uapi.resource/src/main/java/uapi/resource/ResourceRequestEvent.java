/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.common.ArgumentChecker;
import uapi.event.IEvent;

public class ResourceRequestEvent implements IEvent {

    private final String _topic;

    public ResourceRequestEvent(
            final String topic
    ) {
        ArgumentChecker.required(topic, "topic");
        this._topic = topic;
    }

    @Override
    public String topic() {
        return this._topic;
    }
}
