package searchengine.services;

import searchengine.model.entity.SiteEntity;

import java.util.Optional;

public interface SiteService {
    void deleteAllSites();

    SiteEntity parseSiteInConfig();

    SiteEntity findSiteByUrl(String url);


}
