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
 * A net listener is responsible to listen on specific network port to receive request and return response
 */
public interface INetListener {

    /**
     * Start up the net listener
     */
    void startUp() throws NetException;

    /**
     * Shut down the net listener
     */
    void shutDown() throws NetException;
}
