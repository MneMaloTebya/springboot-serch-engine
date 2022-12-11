package searchengine.services.site;

import searchengine.config.Site;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;

import java.util.Optional;

public interface SiteService {

    void deleteByUrl(String url);

    Optional<SiteEntity> findByUrl(String url);

    SiteEntity save(Site site, StatusType type);

    SiteEntity changeStatus(SiteEntity siteEntity, StatusType type);

    SiteEntity updateLastError(SiteEntity siteEntity, String message);
}
