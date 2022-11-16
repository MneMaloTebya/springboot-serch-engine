package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.SiteRepository;
import searchengine.model.entity.SiteEntity;
import searchengine.model.entity.StatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Override
    public void deleteByUrl(String url) {
        siteRepository.deleteByUrl(url);
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
