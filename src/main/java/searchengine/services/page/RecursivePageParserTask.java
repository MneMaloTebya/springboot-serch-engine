package searchengine.services.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import searchengine.model.entity.SiteEntity;
import searchengine.services.indexing.IndexingServiceImpl;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursivePageParserTask extends RecursiveTask<Set<String>> {

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

    private final PageParserService pageParserService;
    private final PageService pageService;
    private final SiteEntity siteEntity;
    private final Set<String> siteUrls;

    public RecursivePageParserTask(PageParserService pageParserService, PageService pageService, SiteEntity siteEntity, Set<String> siteUrls) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;
        this.siteEntity = siteEntity;
        this.siteUrls = siteUrls;
    }

    @Override
    public Set<String> compute() {
        try {
            for (String url : siteUrls) {
                Set<String> siteUrls = pageParserService.parsing(siteEntity, url);
                RecursivePageParserTask task = new RecursivePageParserTask(pageParserService, pageService, siteEntity, siteUrls);
                task.fork();
            }
        } catch (Exception e) {
            log.error("Ошибка парсинга сайта:", e);
        }

        return siteUrls;
    }
}
