/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.service.spring;

import uapi.service.IServiceLoader;

/**
 * The service loader is used to loader which is defined in spring context
 */
public interface ISpringServiceLoader extends IServiceLoader {

    String NAME = "Spring";
}
