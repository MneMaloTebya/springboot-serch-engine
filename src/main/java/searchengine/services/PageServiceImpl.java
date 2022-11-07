package searchengine.services;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.PageRepository;
import searchengine.model.entity.Page;
import searchengine.parser.RecursiveParser;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private static PageRepository pageRepository;

    @Override
    public void goParsing() {
        try {
            new ForkJoinPool().invoke(new RecursiveParser(parsing("http://www.playback.ru/")));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex", "google");

    private final static Object obj = new Object();

    public static Set<String> parsing(String url) throws InterruptedException {
        Thread.sleep(500);
        Set<String> urlSet = new HashSet<>();
        try {
            Document document = getResponse(url).parse();
            Elements elements = document.select("a");
            for (Element element : elements) {
                String link = element.attr("href");

                boolean condition1 = url.startsWith("/");
                boolean condition2 =
                        (url.startsWith("http") || (url.startsWith("https")))
                                && url.contains(getDesiredGroupOfURL(url, 4));
                boolean condition3 = STOP_WORDS.stream().noneMatch(url::contains);

                if (condition1 && condition3) {
                    addEntityToDB(link, urlSet);
                }

                if (condition2 && condition3) {
                    addEntityToDB(link, urlSet);
                }
            }
        } catch (HttpStatusException e) {
            return Collections.EMPTY_SET;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urlSet;
    }

    // TODO: 07.11.2022 тестовый метод
    private static void addEntityToDB(String path, Set<String> urlSet) {
        Page page = new Page();
        synchronized (obj) {
            if (!pageRepository.findPageByPath(path)) {
                urlSet.add(path);
                page.setPath(path);
                page.setContent("content");
                page.setCode(200);
                pageRepository.save(page);
            }
        }
    }

    private static Connection.Response getResponse(String linkPage) throws IOException {
        Connection.Response response = Jsoup.connect(linkPage)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://google.com")
                .timeout(5000)
                .execute();
        return response;
    }

    private static String getDesiredGroupOfURL(String url, int group) {
        Pattern pattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
        String desiredGroup = null;
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            switch (group) {
                case 1:
                    desiredGroup = matcher.group(1);
                    break;
                case 2:
                    desiredGroup = matcher.group(2);
                    break;
                case 3:
                    desiredGroup = matcher.group(3);
                    break;
                case 4:
                    desiredGroup = matcher.group(4);
                    break;
                case 5:
                    desiredGroup = matcher.group(5);
                    break;
            }
        }
        return desiredGroup;
    }

}
