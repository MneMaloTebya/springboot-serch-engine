package searchengine.services.site;

import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.model.SiteRepository;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;
import searchengine.services.page.PageService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final PageService pageService;

    public SiteServiceImpl(SiteRepository siteRepository, PageService pageService) {
        this.siteRepository = siteRepository;
        this.pageService = pageService;
    }

    @Override
    @Transactional
    public void deleteByUrl(String url) {
        SiteEntity siteEntity = findByUrl(url).orElse(null);

        if (siteEntity != null) {
            pageService.deleteBySiteId(siteEntity.getId());
            siteRepository.delete(siteEntity);
        }
    }

    @Override
    public void deleteAll() {
        siteRepository.deleteAll();
    }

    @Override
    public Optional<SiteEntity> findByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    @Override
    public SiteEntity save(Site site, StatusType type) {
        SiteEntity siteEntity = new SiteEntity();
        siteEntity.setName(site.getName());
        siteEntity.setUrl(site.getUrl());
        siteEntity.setStatusType(type);
        siteEntity.setStatusTime(LocalDateTime.now());
        siteRepository.save(siteEntity);
        return siteEntity;
    }

    @Override
    public SiteEntity changeStatus(SiteEntity siteEntity, StatusType type) {
        siteEntity.setStatusType(type);
        siteEntity.setStatusTime(LocalDateTime.now());
        return siteRepository.save(siteEntity);
    }

    @Override
    public SiteEntity updateTime(SiteEntity siteEntity) {
        siteEntity.setStatusTime(LocalDateTime.now());
        return siteRepository.save(siteEntity);
    }

    @Override
    public SiteEntity updateLastError(SiteEntity siteEntity, String message) {
        siteEntity.setLastError(message);
        return siteRepository.save(siteEntity);
    }
}
