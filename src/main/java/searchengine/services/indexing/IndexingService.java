package searchengine.services.indexing;

import searchengine.dto.indexing.StartIndexingResponse;

/**
 * Сервис для индексации сайтов
 */
public interface IndexingService {

    /**
     * Запустить индексацию всех сайтов
     */
    StartIndexingResponse startIndexingAll();


    /**
     * Остановить индексацию всех сайтов
     */
    void stopIndexingAll();
}
