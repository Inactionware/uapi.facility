/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.behavior.IResponsible;

/**
 * A Resource implements this interface will indicate the Resource can contribute behavior
 */
public interface ICapable {

    /**
     * Init resource behavior for the responsible which is the resource representation in the Behavior framework
     *
     * @param   responsible The responsible
     */
    void initBehavior(IResponsible responsible);
}
