package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.PageRepository;

@Service
public class PageServiceImpl implements PageService{

    @Autowired
    private PageRepository pageRepository;

    @Override
    public void deleteAllPages() {
        pageRepository.deleteAll();
    }
}
