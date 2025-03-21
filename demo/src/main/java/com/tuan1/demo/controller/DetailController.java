package com.tuan1.demo.controller;

import com.tuan1.demo.model.Product;
import com.tuan1.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/detail")
@RequiredArgsConstructor
public class DetailController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "detail"; // Load lại trang detail.html
        } else {
            return "redirect:/"; // Nếu không tìm thấy, quay về trang chủ
        }
    }
}
