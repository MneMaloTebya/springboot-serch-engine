package searchengine.services.indexing;

import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.Config;
import searchengine.dto.indexing.ErrorIndexingResponse;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.page.PageService;
import searchengine.services.site.SiteService;

import java.util.Optional;
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
    public IndexingResponse startIndexingAll() {

        if (executor.getActiveCount() > 0) {
            ErrorIndexingResponse response = new ErrorIndexingResponse();
            response.setResult(false);
            response.setError("Индексация уже запущена");
            return response;
        }

        for (Site site : config.getSites()) {
            startSiteIndexing(site);
        }
        IndexingResponse response = new IndexingResponse();
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
    public IndexingResponse stopIndexingAll() {

        if (executor.getActiveCount() == 0) {
            ErrorIndexingResponse response = new ErrorIndexingResponse();
            response.setResult(false);
            response.setError("Индексация не запущена");
            return response;
        } else {
            for (Site site : config.getSites()) {
                Optional<SiteEntity> siteEntity = siteService.findByUrl(site.getUrl());
                if (siteEntity.isPresent()) {
                    SiteEntity entity = siteEntity.get();
                    if (entity.getStatusType().equals(StatusType.INDEXING)) {
                        siteService.changeStatus(entity, StatusType.FAILED);
                    }
                }
            }
            executor.shutdownNow();
            IndexingResponse response = new IndexingResponse();
            response.setResult(true);
            return response;
        }
    }
}
