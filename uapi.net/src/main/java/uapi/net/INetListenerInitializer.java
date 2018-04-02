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
 * The initializer is used to initialize net listener by specific attribute
 */
public interface INetListenerInitializer {

    String METHOD_SET_ATTRIBUTE = "setAttribute";
    String METHOD_NEW_LISTENER  = "newListener";

    /**
     * Set attribute
     *
     * @param   name
     *          The name of attribute
     * @param   value
     *          The value of attribute
     * @return  The initializer self
     */
    INetListenerInitializer setAttribute(String name, Object value);

    /**
     * Return new net listener by currently attribute
     *
     * @return  The net listener
     */
    INetListener newListener();
}
