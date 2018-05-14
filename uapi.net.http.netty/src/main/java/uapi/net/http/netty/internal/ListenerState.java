/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

public enum ListenerState {

    Stopped(0),
    Starting(10),
    Started(20),
    Stopping(30);

    private int _value;

    int value() {
        return this._value;
    }

    ListenerState(int value) {
        this._value = value;
    }
}
