package com.tuan1.demo.controller;

import com.tuan1.demo.model.Category;
import com.tuan1.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách danh mục
    @GetMapping
    public String categoryPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category()); // Đối tượng rỗng để thêm mới
        return "/admin/category/index";
    }

    // Hiển thị form thêm danh mục
    @GetMapping("/create")
    public String createCategoryPage(Model model) {
        model.addAttribute("newCategory", new Category());
        return "/admin/category/create";
    }

    // Xử lý thêm danh mục
    @PostMapping("/create")
    public String createCategory(@ModelAttribute("newCategory") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/category";
    }

    // Hiển thị form chỉnh sửa danh mục
    @GetMapping("/edit/{id}")
    public String editCategoryPage(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "/admin/category/edit";
        }
        return "redirect:/admin/category";
    }

    // Xử lý cập nhật danh mục
    @PostMapping("/edit")
    public String editCategory(@ModelAttribute("category") Category category) {
        categoryService.updateCategory(category);
        return "redirect:/admin/category";
    }

    // Xử lý xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
}
