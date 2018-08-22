/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http;

import uapi.net.NetListenerAttributes;

public class HttpAttributes extends NetListenerAttributes {

    public static final String TYPE     = "HTTP";

    public static final String EVENT_SOURCE = "EventSource";

    private HttpAttributes() { }
}
