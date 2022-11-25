package searchengine.services.page;

import searchengine.model.entity.PageEntity;
import java.util.Optional;

public interface PageService {
    PageEntity save(PageEntity page);
    Optional<PageEntity> findByPath(String path);
    void deleteBySiteId(int siteId);
}
