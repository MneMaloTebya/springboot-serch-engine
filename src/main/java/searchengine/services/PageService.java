package searchengine.services;

import searchengine.model.entity.PageEntity;
import java.util.Optional;

public interface PageService {
    PageEntity save(PageEntity page);
    Optional<PageEntity> findByPath(String path);
}
