package searchengine.services.page;

import searchengine.model.entity.SiteEntity;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursivePageParserService extends RecursiveTask<Set<String>> {

    private PageParserService pageParserService;
    private PageService pageService;
    private SiteEntity siteEntity;
    private Set<String> urlSet;

    public RecursivePageParserService(PageParserService pageParserService, PageService pageService, Set<String> urlSet, SiteEntity siteEntity) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;
        this.siteEntity = siteEntity;
        this.urlSet = urlSet;
    }

    @Override
    public Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {
            List<RecursivePageParserService> tasks = new ArrayList<>();
            for (String url : urlSet) {
                RecursivePageParserService task =
                        new RecursivePageParserService(pageParserService, pageService, pageParserService.parsing(siteEntity, url), siteEntity);
                task.fork();
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }
}
