package searchengine.services.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.model.entity.PageEntity;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.indexing.IndexingServiceImpl;
import searchengine.services.site.SiteService;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageParserServiceImpl implements PageParserService {

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

    private final PageService pageService;
    private final SiteService siteService;

    private final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex",
                    "google", "webp", "zip");

    private final Object object = new Object();

    public PageParserServiceImpl(PageService pageService, SiteService siteService) {
        this.pageService = pageService;
        this.siteService = siteService;
    }

    @Override
    public Set<String> parsing(SiteEntity siteEntity, String currentUrl) throws InterruptedException {
        Thread.sleep(150);
        log.info("PageParserService: старт парсинга страницы " + currentUrl);

        Set<String> urls = new HashSet<>();

        try {
            var response = getResponse(currentUrl);
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
//                    if (!(siteEntity.getUrl() + url).equals(currentUrl)) {
//                        urls.add(siteEntity.getUrl() + url);
//                    }
                    addInsertPageToDB(url, statusCode, content, siteEntity);
                }

                if (condition2 && condition3) {
                    if (!url.equals(currentUrl)) {
                        urls.add(url);
                    }
                    url = getDesiredGroupOfURL(url, 5);
                    addInsertPageToDB(url, statusCode, content, siteEntity);
                }
            }
        } catch (HttpStatusException e) {
            log.error("Ошибка при парсинге сайта: ", e);
//            siteService.changeStatus(siteEntity, StatusType.FAILED);
            siteService.updateLastError(siteEntity, e.getMessage());
            return Collections.emptySet();
        } catch (IOException e) {
            log.error("Ошибка при парсинге сайта: ", e);
//            siteService.changeStatus(siteEntity, StatusType.FAILED);
            siteService.updateLastError(siteEntity, e.getMessage());
            // throw new RuntimeException();
        }

        log.info("PageParserService: завершен парсинг страницы  " + currentUrl + ", урлы на странице: " + urls);

        return urls;
    }

    private void addInsertPageToDB(String url, int code, String content, SiteEntity siteEntity) {
        synchronized (object) {
            Optional<PageEntity> optional = pageService.findByPath(url);
            if (optional.isEmpty()) {
                PageEntity page = new PageEntity();
                page.setPath(url);
                page.setCode(code);
                page.setContent(content);
                page.setSiteId(siteEntity.getId());
                pageService.save(page);
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
