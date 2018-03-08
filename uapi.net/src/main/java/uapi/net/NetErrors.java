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
