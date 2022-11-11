package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.PageService;
import searchengine.services.SiteService;
import searchengine.services.StatisticsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private PageService pageService;

    @Autowired
    private SiteService siteService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public Map<String, String> startIndexing() {
        Map<String, String> response = new HashMap<>();
        pageService.deleteAllPages();
        siteService.deleteAllSites();
        siteService.parseSiteInConfig();



        return response;
    }

}
/**
 * 0) очищаю все таблицы.
 * 1) парсю урлы, имена страниц. добавляю дату, статус и пустой лист из страниц. сохраняю. Для этого,
 * создам отдельный сервис SiteService.
 * 2) дальше начинаю парсить страницы отдельного сайта.
 * 3) беру дефолтный урл. передаю его в параметры форкджоина.
 */