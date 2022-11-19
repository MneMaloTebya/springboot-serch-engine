package searchengine.services.page;

import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursivePageParserService extends RecursiveTask<Set<PageEntity>> {

    private PageParserService pageParserService;
    private PageService pageService;
    private SiteEntity siteEntity;
    private Set<PageEntity> pageEntities;

    public RecursivePageParserService(PageParserService pageParserService, PageService pageService, Set<PageEntity> pageEntities, SiteEntity siteEntity) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;
        this.siteEntity = siteEntity;
        this.pageEntities = pageEntities;
    }

    @Override
    public Set<PageEntity> compute() {
        Set<PageEntity> entities = new HashSet<>();
        try {
            List<RecursivePageParserService> tasks = new ArrayList<>();
            for (PageEntity entity : pageEntities) {
                RecursivePageParserService task =
                        new RecursivePageParserService(pageParserService, pageService, pageParserService.parsing(siteEntity), siteEntity);
                task.fork();
                tasks.add(task);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}
