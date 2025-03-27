package com.tuan1.demo.controller;

import com.tuan1.demo.model.Product;
import com.tuan1.demo.repository.ProductRepository;
import com.tuan1.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductRepository productRepository;
    private final ProductService productService;

    // Danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product/index";
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("brands", productService.getAllBrands());
        return "admin/product/create";
    }

    // Lưu sản phẩm mới
    @PostMapping("/create")
    public String saveProduct(@ModelAttribute @Valid Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                // Lưu file ảnh và lấy đường dẫn
                String imagePath = productService.saveImage(imageFile);
                product.setImage(imagePath);
            }

            product.setQuantity(product.getQuantity());
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được lưu!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh: " + e.getMessage());
        }

        return "redirect:/admin/product";
    }


    // Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("brands", productService.getAllBrands());
        return "admin/product/edit";
    }

    // Cập nhật sản phẩm
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute @Valid Product product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));

            // Nếu có ảnh mới -> cập nhật, nếu không -> giữ nguyên ảnh cũ
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = productService.saveImage(imageFile);
                existingProduct.setImage(imagePath);
            }

            // Cập nhật các thông tin khác
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setGender(product.getGender());
            existingProduct.setQuantity(product.getQuantity());

            productRepository.save(existingProduct);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được cập nhật!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật ảnh: " + e.getMessage());
        }

        return "redirect:/admin/product";
    }

    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm với ID: " + id)
        );
        redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được xóa!");
        return "redirect:/admin/product";
    }




}
