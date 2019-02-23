/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.protocol;

import uapi.GeneralException;
import uapi.behavior.ActionIdentify;
import uapi.behavior.BehaviorFailure;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.common.ArgumentChecker;
import uapi.net.http.HttpEvent;
import uapi.net.http.HttpStatus;
import uapi.rx.Looper;
import uapi.service.annotation.Service;

@Service
@Action
public class ErrorProcessing {

    public static final ActionIdentify actionId = ActionIdentify.toActionId(ErrorProcessing.class);

    @ActionDo
    public void process(
            final BehaviorFailure failure
    ) {
        ArgumentChecker.required(failure, "failure");

        Exception exception = failure.cause();
        Object[] failureInputs = failure.failureInputs();
        Looper.on(failureInputs).foreach(failureInput -> {
            if (failureInput instanceof ResourceProcessing) {
                ResourceProcessing processing = (ResourceProcessing) failureInput;
                IProtocolEncoder encoder = processing.encoder();
                if (encoder == null) {
                    throw ProtocolException.builder()
                            .errorCode(ProtocolErrors.ENCODER_NOT_DEFINED)
                            .variables(processing.originalRequest())
                            .build();
                }
                processing = encoder.encodeError(exception, processing);
                processing.originalResponse().flush();
            } else if (failureInput instanceof HttpEvent) {
                HttpEvent httpEvent = (HttpEvent) failureInput;
                httpEvent.response().setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                httpEvent.response().flush();
            } else {
                throw new GeneralException("Unsupported failure input - {}", failureInput.getClass().getCanonicalName());
            }
        });
    }
}
