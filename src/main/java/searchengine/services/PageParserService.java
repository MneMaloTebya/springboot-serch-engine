package searchengine.services;

import searchengine.model.entity.SiteEntity;

import java.util.Set;

public interface PageParserService {
//    void startIndexing() throws InterruptedException;
    Set<String> parsing(SiteEntity siteEntity) throws InterruptedException;
}
