package com.example.BookHealthServiceOnline.service.impl;

import com.example.BookHealthServiceOnline.dao.DocDeptCategoryDao;
import com.example.BookHealthServiceOnline.domain.DocDeptCategory;
import com.example.BookHealthServiceOnline.service.DocDeptCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocDeptCategoryServiceImpl implements DocDeptCategoryService {

    @Autowired
    private DocDeptCategoryDao docDeptCategoryDao;

    @Override
    public DocDeptCategory save(DocDeptCategory docDeptCategory) {
        return docDeptCategoryDao.save(docDeptCategory);
    }

    @Override
    public DocDeptCategory findById(Long id) {
        return docDeptCategoryDao.findById(id);
    }

    @Override
    public List<DocDeptCategory> findAll() {
        return docDeptCategoryDao.findAll();
    }

    @Override
    public DocDeptCategory update(DocDeptCategory docDeptCategory) {
        if (docDeptCategory.getId() != null) {
            return docDeptCategoryDao.update(docDeptCategory);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        docDeptCategoryDao.delete(id);
    }
}
