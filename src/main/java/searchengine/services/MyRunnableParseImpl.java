package searchengine.services;

import searchengine.model.entity.SiteEntity;

import java.util.concurrent.ForkJoinPool;

public class MyRunnableParseImpl implements Runnable {

    private PageParserService pageParserService;
    private RecursivePageParserServiceImpl recursivePageParserService;
    private PageService pageService;

    public MyRunnableParseImpl(PageParserService pageParserService, SiteEntity siteEntity, PageService pageService) {
        this.pageParserService = pageParserService;
        this.pageService = pageService;

        try {
            recursivePageParserService = new RecursivePageParserServiceImpl(pageParserService, pageService, pageParserService.parsing(siteEntity), siteEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new ForkJoinPool().invoke(recursivePageParserService);
    }
}
