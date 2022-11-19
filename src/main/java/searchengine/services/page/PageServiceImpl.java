package searchengine.services.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.PageRepository;
import searchengine.model.entity.PageEntity;

import java.util.Optional;

@Service
public class PageServiceImpl implements PageService{

    @Autowired
    private PageRepository pageRepository;

    @Override
    public PageEntity save(PageEntity page) {
        return pageRepository.save(page);
    }

    @Override
    public Optional<PageEntity> findByPath(String path) {
        return pageRepository.findPageByPath(path);
    }
}
