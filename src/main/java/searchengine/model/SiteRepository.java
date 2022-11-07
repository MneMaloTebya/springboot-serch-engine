package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.Site;

public interface SiteRepository extends JpaRepository<Site, Integer> {
}
