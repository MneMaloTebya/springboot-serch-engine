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
public class SiteServiceImpl implements SiteService{

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SitesList sitesList;

    @Override
    public void deleteAllSites() {
        siteRepository.deleteAll();
    }

    @Override
    public SiteEntity parseSiteInConfig() {
        List<Site> sites = sitesList.getSites();
        SiteEntity siteEntity = new SiteEntity();
        for (int i = 0; i <sites.size(); i++) {
            siteEntity.setName(sites.get(i).getName());
            siteEntity.setUrl(sites.get(i).getUrl());
            siteEntity.setStatusTime(LocalDateTime.now());
            siteEntity.setPages(null);
            siteEntity.setStatusType(StatusType.INDEXED);
            siteRepository.save(siteEntity);
        }
        return siteEntity;
    }

    @Override
    public SiteEntity findSiteByUrl(String url) {
        SiteEntity siteEntity = null;
        Optional<SiteEntity> optional = siteRepository.findSiteEntityByUrl(url);
        if (optional.isPresent()) {
            siteEntity = optional.get();
        }
        return siteEntity;
    }
}
