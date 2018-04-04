/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http;

import uapi.net.IRequest;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface IHttpRequest extends IRequest {

    HttpVersion version();

    HttpMethod method();

    String uri();

    String path();

    ContentType contentType();

    Charset charset();

    Iterator<Map.Entry<String, String>> headers();

    String header(String key);

    Iterator<Map.Entry<String, List<String>>> params();

    List<String> param(String key);

    boolean isKeepAlive();
}
