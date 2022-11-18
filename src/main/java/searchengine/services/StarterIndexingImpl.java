package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;

import java.util.List;

@Service
public class StarterIndexingImpl implements StartIndexing {

    @Autowired
    private SitesList sitesList;

    @Autowired
    private SiteService siteService;

    @Autowired
    private PageService pageService;

    @Autowired
    private PageParserService pageParserService;

    @Override
    public void startIndexing() {
        List<Site> sites = sitesList.getSites();
        for (Site site : sites) {
            SiteEntity siteEntity = siteService.save(site, StatusType.INDEXING);
            Thread thread = new Thread(new MyRunnableParseImpl(pageParserService, siteEntity, pageService));
            thread.start();
            siteService.changeStatus(siteEntity, StatusType.INDEXED);
        }
    }
}
