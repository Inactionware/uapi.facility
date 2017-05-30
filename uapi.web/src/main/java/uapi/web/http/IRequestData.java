package uapi.web.http;

/**
 * Created by min on 2017/5/29.
 */
public interface IRequestData {

    String[] mappingUrls();

    IDataInfo[] dataInfos();

    interface IDataInfo {}

    class UrlDataInfo implements IDataInfo {

        int index;
    }

    class QueryDataInfo implements IDataInfo {

        String queryKey;
    }

    class PostDataInfo implements IDataInfo {

        String postKey;
    }

    class HeaderDataInfo implements IDataInfo {

        String headerKey;
    }

    enum FromType {
        Uri, Query, Post, Header
    }
}
