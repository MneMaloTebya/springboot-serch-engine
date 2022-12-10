package searchengine.services.indexing;

import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.Config;
import searchengine.dto.indexing.ErrorStartIndexingResponse;
import searchengine.dto.indexing.StartIndexingResponse;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.page.PageService;
import searchengine.services.site.SiteService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class IndexingServiceImpl implements IndexingService {

    private final Config config;
    private final SiteService siteService;
    private final PageService pageService;
    private final ThreadPoolExecutor executor;

    public IndexingServiceImpl(Config config, SiteService siteService, PageService pageService) {
        this.config = config;
        this.siteService = siteService;
        this.pageService = pageService;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.getSites().size());
    }

    @Override
    public StartIndexingResponse startIndexingAll() {

        if (executor.getActiveCount() > 0) {
            ErrorStartIndexingResponse response = new ErrorStartIndexingResponse();
            response.setResult(false);
            response.setError("Индексация уже запущена");
            return response;
        }

        for (Site site : config.getSites()) {
            startSiteIndexing(site);
        }
        StartIndexingResponse response = new StartIndexingResponse();
        response.setResult(true);
        return response;
    }

    private void startSiteIndexing(Site site) {
        siteService.deleteByUrl(site.getUrl());
        SiteEntity siteEntity = siteService.save(site, StatusType.INDEXING);
        SiteIndexingThread siteIndexingThread = new SiteIndexingThread(pageService, siteService, siteEntity, site.getUrl());
        executor.execute(siteIndexingThread);
    }

    @Override
    public void stopIndexingAll() {
        /**
         * на этом этапе я должен понять с каким сайтом работает мой поток и присвоить ему статус FAILED и прописать текст ошибки
         */
        executor.shutdownNow();
    }

}
