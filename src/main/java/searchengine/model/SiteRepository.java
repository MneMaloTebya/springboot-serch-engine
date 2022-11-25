package searchengine.model;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.model.entity.SiteEntity;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

    //    @Query("DELETE FROM site where url=:url")
    void deleteByUrl(String url);

    Optional<SiteEntity> findByUrl(String url);
}
