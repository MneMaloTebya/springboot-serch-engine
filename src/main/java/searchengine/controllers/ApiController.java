package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.message.MyMessage;
import searchengine.services.indexing.IndexingService;
import searchengine.services.StatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private IndexingService indexingService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public String startIndexing() {
        MyMessage message = new MyMessage(true);
        if (indexingService.isStarted()) {
            message = new MyMessage(false, "Индексация уже запущена.");
        }
        else {
            indexingService.startIndexingAll();
        }
        return message.toString();
    }

    @GetMapping("/stopIndexing")
    public String stopIndexing() {
        indexingService.stopIndexingAll();
        /**
         * доработать метод. добавить респонс
         */
        return null;
    }
}