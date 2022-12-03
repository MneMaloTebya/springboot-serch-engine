package searchengine.services.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.page.PageParserService;
import searchengine.services.page.PageParserServiceImpl;
import searchengine.services.page.PageService;
import searchengine.services.page.RecursivePageParserTask;
import searchengine.services.site.SiteService;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class SiteIndexingThread implements Runnable {

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

    private final PageParserService pageParserService;
    private final SiteEntity siteEntity;
    private final RecursivePageParserTask recursivePageParserTask;
    private final SiteService siteService;

    public SiteIndexingThread(PageService pageService,
                              SiteService siteService,
                              SiteEntity siteEntity,
                              String currentUrl) {
        this.siteEntity = siteEntity;
        this.siteService = siteService;
        this.pageParserService = new PageParserServiceImpl(pageService, siteService);

        try {
            Set<String> siteDataMainPage = pageParserService.parsing(siteEntity, currentUrl);

            log.info("Запускаем парсинг по сайтам: " + siteDataMainPage);
            recursivePageParserTask = new RecursivePageParserTask(pageParserService, pageService, siteEntity, siteDataMainPage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            new ForkJoinPool().invoke(recursivePageParserTask);
            siteService.changeStatus(siteEntity, StatusType.INDEXED);
        } catch (Exception e) {
            log.error("Ошибка в SiteIndexingRunnable:", e);
        }

//        recursivePageParserService.compute();
    }
}
