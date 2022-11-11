package searchengine.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

@Component
public class RecursivePageParser extends RecursiveTask<Set<String>> {

    private Set<String> urlsSet;
    private PageParser pageParser;

    @Autowired
    public RecursivePageParser(Set<String> urlsSet, PageParser pageParser) {
        this.urlsSet = urlsSet;
        this.pageParser = pageParser;
    }

    @Override
    protected Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {
            List<RecursivePageParser> tasks = new ArrayList<>();
            for (String url : urls) {
                RecursivePageParser task = new RecursivePageParser(pageParser.parsing(url), pageParser);
                task.fork();
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }
}
