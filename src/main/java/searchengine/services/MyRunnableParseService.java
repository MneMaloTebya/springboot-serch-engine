package searchengine.services;

import searchengine.model.entity.SiteEntity;
import searchengine.services.page.PageParserService;
import searchengine.services.page.PageService;
import searchengine.services.page.RecursivePageParserService;

import java.util.concurrent.ForkJoinPool;

public class MyRunnableParseService implements Runnable {

    private PageParserService pageParserService;
    private RecursivePageParserService recursivePageParserService;
    private PageService pageService;

    public MyRunnableParseService(PageParserService pageParserService, SiteEntity siteEntity,
                                  PageService pageService) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;

        try {
            recursivePageParserService = new RecursivePageParserService(
                    pageParserService,
                    pageService,
                    pageParserService.parsing(siteEntity),
                    siteEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new ForkJoinPool().invoke(recursivePageParserService);
    }
}
