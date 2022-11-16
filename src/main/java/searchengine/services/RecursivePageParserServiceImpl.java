package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.entity.SiteEntity;

import java.util.*;
import java.util.concurrent.RecursiveTask;

@Service
public class RecursivePageParserServiceImpl extends RecursiveTask<Set<String>> implements RecursivePageParserService {

    private Set<String> urlsSet;

    @Autowired
    private PageParserService pageParserService;

    @Autowired
    private SiteService siteService;

    @Autowired
    public RecursivePageParserServiceImpl(Set<String> urlsSet) {
        this.urlsSet = urlsSet;
    }

    @Override
    public Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {
            List<RecursivePageParserServiceImpl> tasks = new ArrayList<>();
            for (String url : urls) {
                Optional<SiteEntity> optional = siteService.findByUrl(url);
                if (optional.isPresent()) {
                    SiteEntity siteEntity = optional.get();
                    RecursivePageParserServiceImpl task =
                            new RecursivePageParserServiceImpl(pageParserService.parsing(siteEntity));
                    task.fork();
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }
}
