package searchengine.services.indexing;

/**
 * Сервис для индексации сайтов
 */
public interface IndexingService {

    /**
     * Запустить индексацию всех сайтов
     */
    void startIndexingAll();

    boolean isStarted();

    /**
     * Остановить индексацию всех сайтов
     */
    void stopIndexingAll();
}
