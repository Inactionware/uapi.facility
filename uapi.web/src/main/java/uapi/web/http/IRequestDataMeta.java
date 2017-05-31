package uapi.web.http;

/**
 * Created by min on 2017/5/29.
 */
public interface IRequestDataMeta {

    String[] mappingUrls();

    FieldInfo[] fieldInfs();

    Class<?> type();
}
