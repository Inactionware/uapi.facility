package uapi.web.http;

import uapi.GeneralException;
import uapi.rx.Looper;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for request data meta
 */
@Service
public class RequestDataMetaRegistry {

    @Inject
    @Optional
    protected List<IRequestDataMeta> _reqDataMetas;

    private final Map<String, IRequestDataMeta> _urlDataMetaMapCache = new ConcurrentHashMap<>();

    public IRequestDataMeta getData(final String requestUrl) {
        IRequestDataMeta matchedMeta = this._urlDataMetaMapCache.get(requestUrl);
        if (matchedMeta == null) {
            List<IRequestDataMeta> matchedMetas = Looper.on(this._reqDataMetas)
                    .filter(dataMeta -> {
                        String[] mappingUris = dataMeta.mappingUrls();
                        List<String> matchedUris = Looper.on(mappingUris)
                                .filter(requestUrl::startsWith)
                                .toList();
                        return matchedUris.size() > 0;
                    }).toList();
            if (matchedMetas.size() == 0) {
                throw new GeneralException("No mapped data meta for request: {}", requestUrl);
            } else if (matchedMetas.size() == 1) {
                matchedMeta = matchedMetas.get(0);
                this._urlDataMetaMapCache.put(requestUrl, matchedMetas.get(0));
            } else {
                UriDataMetaMapping uriMetaMapping = Looper.on(matchedMetas)
                        .map(meta -> {
                            String matchedUri = Looper.on(meta.mappingUrls())
                                    .filter(requestUrl::startsWith)
                                    .select((item, selected) ->
                                            selected == null || item.length() > selected.length());
                            return new UriDataMetaMapping(matchedUri, meta);
                        }).select((item, selected) ->
                                selected == null || item.uri().length() > selected.uri().length());
                matchedMeta = uriMetaMapping.dataMeta();
                this._urlDataMetaMapCache.put(uriMetaMapping.uri(), matchedMeta);
            }
        }

        if (matchedMeta == null) {
            throw new GeneralException("No mapped data meta for request: {}", requestUrl);
        }

        return matchedMeta;
    }

    public List<String> getMappedUrls() {
        return Looper.on(this._reqDataMetas)
                .flatmap(dataMeta -> Looper.on(dataMeta.mappingUrls()))
                .toList();
    }

    private final class UriDataMetaMapping {

        private final String _uri;
        private final IRequestDataMeta _dataMeta;

        private UriDataMetaMapping(String uri, IRequestDataMeta dataMeta) {
            this._uri = uri;
            this._dataMeta = dataMeta;
        }

        public String uri() {
            return this._uri;
        }

        public IRequestDataMeta dataMeta() {
            return this._dataMeta;
        }
    }
}
