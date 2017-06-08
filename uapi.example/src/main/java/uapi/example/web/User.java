package uapi.example.web;

import uapi.web.http.annotation.*;

/**
 * Created by xquan on 6/8/2017.
 */
@RequestData
@MappingUris("/hello")
public class User {

    @FromUri(1)
    @Required
    @Size(min=3, max=6)
    protected String _name;
}
