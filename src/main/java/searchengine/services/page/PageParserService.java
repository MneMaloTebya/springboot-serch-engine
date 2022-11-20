package searchengine.services.page;

import searchengine.model.entity.SiteEntity;

import java.util.Set;

public interface PageParserService {
    Set<String> parsing(SiteEntity siteEntity, String currentUrl) throws InterruptedException;
}
