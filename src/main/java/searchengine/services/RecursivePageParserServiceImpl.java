package searchengine.services;

import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursivePageParserServiceImpl extends RecursiveTask<Set<PageEntity>> implements RecursivePageParserService {

    private PageParserService pageParserService;

    private PageService pageService;

    private SiteEntity siteEntity;

    private Set<PageEntity> pageEntities;

    public RecursivePageParserServiceImpl(PageParserService pageParserService, PageService pageService, Set<PageEntity> pageEntities, SiteEntity siteEntity) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;
        this.siteEntity = siteEntity;
        this.pageEntities = pageEntities;
    }

    @Override
    public Set<PageEntity> compute() {
        Set<PageEntity> entities = new HashSet<>();
        try {
            List<RecursivePageParserServiceImpl> tasks = new ArrayList<>();
            for (PageEntity entity : entities) {
                Optional<PageEntity> optional = pageService.findByPath(entity.getPath());
                if (optional.isPresent()) {
                    PageEntity pageEntity = optional.get();
                    RecursivePageParserServiceImpl task =
                            new RecursivePageParserServiceImpl(pageParserService, pageService, pageParserService.parsing(siteEntity), siteEntity);
                    task.fork();
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}
