package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.page.PageParserService;
import searchengine.services.page.PageService;
import searchengine.services.site.SiteService;

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
        siteService.deleteAll(); //удалить после правки метода deleteByUrl
        for (Site site : sites) {
            SiteEntity siteEntity = siteService.save(site, StatusType.INDEXING);
//            siteService.deleteByUrl(site.getUrl()); не работает
            Thread thread = new Thread(new MyRunnableParseService(pageParserService, siteEntity, pageService));
            thread.start();
            siteService.changeStatus(siteEntity, StatusType.INDEXED);
        }
    }
}
