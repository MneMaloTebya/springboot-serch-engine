package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.PageEntity;

public interface PageRepository extends JpaRepository<PageEntity, Integer> {
    boolean findPageByPath(String path);

}
