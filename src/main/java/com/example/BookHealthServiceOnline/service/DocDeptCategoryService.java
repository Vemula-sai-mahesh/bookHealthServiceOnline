package com.example.BookHealthServiceOnline.service;

import com.example.BookHealthServiceOnline.domain.DocDeptCategory;

import java.util.List;

public interface DocDeptCategoryService {

    DocDeptCategory save(DocDeptCategory docDeptCategory);
    DocDeptCategory update(DocDeptCategory docDeptCategory);
    DocDeptCategory findById(Long id);
    List<DocDeptCategory> findAll();
    void delete(Long id);
}
