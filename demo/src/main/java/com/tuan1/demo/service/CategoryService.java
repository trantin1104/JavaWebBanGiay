package com.tuan1.demo.service;

import com.tuan1.demo.model.Category;
import com.tuan1.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Lưu danh mục vào database.
     * @param category Đối tượng danh mục cần lưu.
     */
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }


    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null); // Trả về null nếu không tìm thấy
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category); // save() hoạt động như update nếu ID đã tồn tại
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
