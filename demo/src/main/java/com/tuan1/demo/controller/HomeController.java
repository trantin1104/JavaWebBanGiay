package com.tuan1.demo.controller;

import com.tuan1.demo.model.Brand;
import com.tuan1.demo.model.Product;
import com.tuan1.demo.repository.BrandRepository;
import com.tuan1.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping({"/", "/page{pageNumber}"})
    public String home(Model model,
                       @PathVariable(value = "pageNumber", required = false) Integer pageNumber,
                       @RequestParam(value = "keyword", required = false) String keyword) {

        addBrandListToModel(model);

        int page = (pageNumber != null && pageNumber > 0) ? pageNumber - 1 : 0;
        int size = 9;
        String sortBy = "id";
        String sortDir = "asc";

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = (keyword != null && !keyword.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(keyword, pageable)
                : productRepository.findAll(pageable);

        int totalPages = productPage.getTotalPages();
        totalPages = (totalPages < 1) ? 1 : totalPages;

        List<Product> products = productPage.getContent();
        if (products.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm nào!");
        }

        model.addAttribute("products", products);
        model.addAttribute("currentPage", totalPages > 0 ? page + 1 : 1);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);
        model.addAttribute("keyword", keyword);

        return "index";
    }

    @GetMapping("/brand/{brandName}")
    public String filterByBrand(@PathVariable String brandName, Model model) {
        addBrandListToModel(model);

        List<Product> products = productRepository.findByBrand_Name(brandName);
        model.addAttribute("products", products);
        model.addAttribute("selectedBrand", brandName);
        model.addAttribute("totalPages", 1);
        return "index";
    }


    private void addBrandListToModel(Model model) {
        List<Brand> brands = brandRepository.findAll();
        model.addAttribute("brands", brands);
    }
}
