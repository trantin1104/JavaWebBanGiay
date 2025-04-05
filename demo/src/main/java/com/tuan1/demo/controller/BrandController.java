package com.tuan1.demo.controller;

import com.tuan1.demo.model.Brand;
import com.tuan1.demo.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    // Hiển thị danh sách thương hiệu
    @GetMapping
    public String brandPage(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("newBrand", new Brand());  // Đối tượng rỗng để thêm mới
        return "admin/brand/index";
    }


    @GetMapping("/create")
    public String createBrandPage(Model model) {
        model.addAttribute("newBrand", new Brand());
        return "admin/brand/create";
    }


    // Xử lý thêm thương hiệu
    @PostMapping("/create")
    public String createBrand(@ModelAttribute("newBrand") Brand brand) {
        brandService.saveBrand(brand);
        return "redirect:/admin/brand";
    }

    // Xử lý xóa thương hiệu
    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return "redirect:/admin/brand";
    }

    // Hiển thị trang chỉnh sửa thương hiệu
    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandService.getBrandById(id));
        return "admin/brand/edit";
    }

    // Xử lý cập nhật thương hiệu
    @PostMapping("/edit")
    public String updateBrand(@ModelAttribute Brand brand) {
        brandService.updateBrand(brand);
        return "redirect:/admin/brand";
    }
}
