package searchengine.services.page;

import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;

import java.util.Set;

public interface PageParserService {
    Set<PageEntity> parsing(SiteEntity siteEntity) throws InterruptedException;
}
