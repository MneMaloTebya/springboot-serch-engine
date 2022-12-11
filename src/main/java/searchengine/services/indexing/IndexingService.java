package searchengine.services.indexing;

import searchengine.dto.indexing.IndexingResponse;

/**
 * Сервис для индексации сайтов
 */
public interface IndexingService {

    /**
     * Запустить индексацию всех сайтов
     */
    IndexingResponse startIndexingAll();


    /**
     * Остановить индексацию всех сайтов
     */
    IndexingResponse stopIndexingAll();
}
