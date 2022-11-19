package searchengine.services.page;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.site.SiteService;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PageParserServiceImpl implements PageParserService {

    @Autowired
    private PageService pageService;

    @Autowired
    private SiteService siteService;

    private final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex",
                    "google", "webp", "zip");

    private final Object object = new Object();

    @Override
    public Set<PageEntity> parsing(SiteEntity siteEntity) throws InterruptedException {
        Thread.sleep(500);
        Set<PageEntity> pageEntities = new HashSet<>();
        try {
            var response = getResponse(siteEntity.getUrl());
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
                    addInsertPageToDB(pageEntities, url, statusCode, content, siteEntity);
                }
                if (condition2 && condition3) {
                    url = getDesiredGroupOfURL(url, 5);
                    addInsertPageToDB(pageEntities, url, statusCode, content, siteEntity);
                }
            }
        } catch (HttpStatusException e) {
            siteService.changeStatus(siteEntity, StatusType.FAILED);
            siteService.updateLastError(siteEntity, e.getMessage());
            return Collections.EMPTY_SET;
        } catch (IOException e) {
            siteService.changeStatus(siteEntity, StatusType.FAILED);
            siteService.updateLastError(siteEntity, e.getMessage());
            throw new RuntimeException();
        }
        return pageEntities;
    }

    private void addInsertPageToDB(Set<PageEntity> pageEntities, String url, int code, String content, SiteEntity siteEntity) {
        synchronized (object) {
            Optional<PageEntity> optional = pageService.findByPath(url);
            if (optional.isEmpty()) {
                PageEntity page = new PageEntity();
                page.setPath(url);
                page.setCode(code);
                page.setContent(content);
                page.setSite(siteEntity);
                pageService.save(page);
                siteService.updateTime(siteEntity);
                pageEntities.add(page);
            }
        }
    }

    private Connection.Response getResponse(String linkPage) throws IOException {
        Connection.Response response = Jsoup.connect(linkPage)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://google.com")
                .timeout(0)
                .execute();
        return response;
    }

    private String getDesiredGroupOfURL(String url, int group) {
        Pattern pattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
        String desiredGroup = "";
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            switch (group) {
                case 1: desiredGroup = matcher.group(1); //https:
                    break;
                case 2 : desiredGroup = matcher.group(2);//https
                    break;
                case 3 : desiredGroup = matcher.group(3);// //siteName.ru
                    break;
                case 4 : desiredGroup = matcher.group(4);// siteName.ru
                    break;
                case 5 : desiredGroup = matcher.group(5);// /page1/next-page/...
                    break;
            }
        }
        return desiredGroup;
    }
}
