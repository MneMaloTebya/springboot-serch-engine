package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.entity.SiteEntity;

import java.util.concurrent.ForkJoinPool;

@Service
public class MyRunnableParseImpl implements Runnable {

    @Autowired
    private PageParserService pageParserService;

    @Autowired
    private RecursivePageParserServiceImpl recursivePageParserService;


    public MyRunnableParseImpl(SiteEntity siteEntity) {
        try {
            recursivePageParserService = new RecursivePageParserServiceImpl(pageParserService.parsing(siteEntity));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new ForkJoinPool().invoke(recursivePageParserService);
    }
}
