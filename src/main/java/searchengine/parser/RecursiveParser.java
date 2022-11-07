package searchengine.parser;

import org.springframework.stereotype.Component;
import searchengine.services.PageServiceImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

@Component
public class RecursiveParser extends RecursiveTask<Set<String>> {

    private Set<String> urlsSet;

    public RecursiveParser(Set<String> urlsSet) {
        this.urlsSet = urlsSet;
    }

    @Override
    public Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {
            for (String url : urlsSet) {
                RecursiveParser task = new RecursiveParser(PageServiceImpl.parsing(url));
                task.fork();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }
}
