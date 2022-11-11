package searchengine.parser;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import searchengine.model.PageRepository;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.services.SiteService;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PageParser {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private SiteService siteService;

    private final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex",
                    "google", "webp", "zip");

    private final Object object = new Object();

    public Set<String> parsing(String currentUrl) throws InterruptedException {
        Thread.sleep(500);
        Set<String> urlSet = new HashSet<>();
        try {
            var response = PageParser.getResponse(currentUrl);
            Document document = response.parse();
            String content = document.outerHtml();
            int statusCode = response.statusCode();
            Elements elements = document.select("a");
            for (Element element : elements) {
                String url = element.attr("href");
                boolean condition1 = url.startsWith("/");
                boolean condition2 = (url.contains(document.location()));
                boolean condition3 = STOP_WORDS.stream().noneMatch(url::contains);
                if (condition1 && condition3) {
                    addInsertPageToDB(urlSet, url, statusCode, content);
                }
                if (condition2 && condition3) {
                    addInsertPageToDB(urlSet, url, statusCode, content);
                }
            }
        } catch (HttpStatusException e) {
            return Collections.EMPTY_SET;
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return urlSet;
    }

    private void addInsertPageToDB(Set<String> urlSet, String url, int code, String content) {


        PageEntity page = new PageEntity();
        synchronized (object) {
            if (!pageRepository.findPageByPath(url)) {
                urlSet.add(url);
                // TODO: 10.11.2022 обрезать урл в путь
                page.setPath(url);
                page.setCode(code);
                page.setContent(content);
                pageRepository.save(page);
            }
        }
    }

    private static Connection.Response getResponse(String linkPage) throws IOException {
        Connection.Response response = Jsoup.connect(linkPage)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://google.com")
                .timeout(0)
                .execute();
        return response;
    }


    public String getDesiredGroupOfURL(String url, int group) {
        Pattern pattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
        String desiredGroup = null;
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            switch (group) {
                case 1: desiredGroup = matcher.group(1);
                    break;
                case 2 : desiredGroup = matcher.group(2);
                    break;
                case 3 : desiredGroup = matcher.group(3);
                    break;
                case 4 : desiredGroup = matcher.group(4);
                    break;
                case 5 : desiredGroup = matcher.group(5);
                    break;
            }
        }
        return desiredGroup;
    }
}
