package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
    boolean findPageByPath(String path);

}
