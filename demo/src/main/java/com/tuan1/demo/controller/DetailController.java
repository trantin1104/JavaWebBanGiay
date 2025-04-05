package com.tuan1.demo.controller;

import com.tuan1.demo.model.Product;
import com.tuan1.demo.repository.BrandRepository;
import com.tuan1.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/detail")
@RequiredArgsConstructor
public class DetailController {

    private final ProductService productService;



    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);



        List<String> docbanners = Arrays.asList("d5.jpg", "d1.jpg", "d2.jpg","d6.jpg", "d3.jpg", "d4.jpg", "d7.jpg");

        model.addAttribute("docbanners", docbanners);

        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "detail"; // Load lại trang detail.html
        } else {
            return "redirect:/"; // Nếu không tìm thấy, quay về trang chủ
        }
    }
}
