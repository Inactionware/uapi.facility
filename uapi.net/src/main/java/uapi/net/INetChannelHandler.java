/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net;

/**
 * A network channel is used for communication between peers
 */
public interface INetChannelHandler {

    /**
     * Invoked when the channel is activated
     *
     * @param   ctx
     *          The context of the channel
     */
    void onActivate(INetChannelContext ctx);

    /**
     * Invoked when the channel is ready to read
     *
     * @param   ctx
     *          The context of the channel
     */
    void onRead(INetChannelContext ctx);

    /**
     * Invoked when the channel throw an exception
     *
     * @param   ctx
     *          The context of the channel
     * @param   throwable
     *          The exception object
     */
    void onException(INetChannelContext ctx, Throwable throwable);
}
