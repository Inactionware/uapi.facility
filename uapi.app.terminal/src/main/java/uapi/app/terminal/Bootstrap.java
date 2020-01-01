/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.app.terminal;

import uapi.UapiException;
import uapi.app.AppErrors;
import uapi.app.AppException;
import uapi.app.SystemBootstrap;
import uapi.app.SystemShuttingDownEvent;
import uapi.app.terminal.internal.CliConfigProvider;
import uapi.event.IAttributedEventHandler;
import uapi.event.IEventBus;
import uapi.service.IRegistry;
import uapi.service.IService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * The UAPI application entry point
 * The Bootstrap's responsibility is load basic services, parse command line arguments and
 * send out system startup event
 */
public class Bootstrap extends SystemBootstrap {

    private static final Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        new Bootstrap(args).boot();
    }

    private final String[] _args;

    @Override
    protected void loadConfig(final IRegistry registry) {
        var cliCfgProvider = registry().findService(CliConfigProvider.class);
        if (cliCfgProvider == null) {
            throw AppException.builder()
                    .errorCode(AppErrors.SPECIFIC_SERVICE_NOT_FOUND)
                    .variables(new AppErrors.SpecificServiceNotFound()
                            .serviceType(CliConfigProvider.class.getCanonicalName()))
                    .build();
        }
        cliCfgProvider.parse(this._args);
    }

    @Override
    protected void beforeSystemLaunching(
            final IRegistry registry,
            final List<IService> appServices
    ) {
        var eventBus = registry().findService(IEventBus.class);
        eventBus.register(new ExitSystemRequestHandler());
    }

    @Override
    protected void afterSystemLaunching(
            final IRegistry registry,
            final List<IService> appServices
    ) {
        Exception ex = null;
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
            semaphore.acquire();
        } catch (InterruptedException e) {
            ex = e;
        }

        // Send system shutting down event
        var eventBus = registry().findService(IEventBus.class);
        SystemShuttingDownEvent shuttingDownEvent = new SystemShuttingDownEvent(appServices, ex);
        eventBus.fire(shuttingDownEvent, true);
    }

    // Private constructor
    private Bootstrap(final String[] args) {
        this._args = args;
    }

    private static final class ShutdownHook implements Runnable {

        @Override
        public void run() {
            semaphore.release();
        }
    }

    private static final class ExitSystemRequestHandler implements IAttributedEventHandler<ExitSystemRequest> {

        @Override
        public String topic() {
            return ExitSystemRequest.TOPIC;
        }

        @Override
        public void handle(ExitSystemRequest event) throws UapiException {
            Bootstrap.semaphore.release();
        }

        @Override
        public Map<Object, Object> getAttributes() {
            return null;
        }
    }
}
