package searchengine.services;

import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;

import java.util.Set;

public interface RecursivePageParserService {
    Set<PageEntity> compute();
}
