package com.example.BookHealthServiceOnline.dao;

import com.example.BookHealthServiceOnline.domain.DocDeptCategory;

import java.util.List;

public interface DocDeptCategoryDao {
    DocDeptCategory save(DocDeptCategory docDeptCategory);
    DocDeptCategory update(DocDeptCategory docDeptCategory);
    DocDeptCategory findById(Long id);
    List<DocDeptCategory> findAll();
    void delete(Long id);
}
