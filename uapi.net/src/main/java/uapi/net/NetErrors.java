/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net;

import uapi.exception.FileBasedExceptionErrors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetErrors extends FileBasedExceptionErrors<NetException> {

    public static final int CATEGORY    = 0x0201;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
    }

    @Override
    protected String getFile(NetException ex) {
        if (ex.category() == CATEGORY) {
            return "/netErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(NetException ex) {
        return keyCodeMapping.get(ex.errorCode());
    }
}
