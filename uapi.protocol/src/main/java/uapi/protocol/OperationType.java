/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

public class OperationType {

    public static final int QUERY   = 0x1;
    public static final int CREATE  = 0x2;
    public static final int REMOVE  = 0x4;
    public static final int MODIFY  = 0x8;

    private OperationType() { }
}
