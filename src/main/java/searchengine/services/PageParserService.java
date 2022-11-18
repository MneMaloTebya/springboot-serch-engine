package searchengine.services;

import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;

import java.util.Set;

public interface PageParserService {
//    void startIndexing() throws InterruptedException;
    Set<PageEntity> parsing(SiteEntity siteEntity) throws InterruptedException;
}
