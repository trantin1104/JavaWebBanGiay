package com.tuan1.demo.service;

import com.tuan1.demo.model.Brand;
import com.tuan1.demo.model.Category;
import com.tuan1.demo.model.Product;
import com.tuan1.demo.repository.BrandRepository;
import com.tuan1.demo.repository.CategoryRepository;
import com.tuan1.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            product.setImage(imagePath);
        }

        // Đảm bảo số lượng không âm
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm không hợp lệ!");
        }

        return productRepository.save(product);
    }


    public Page<Product> getAllProductsPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }



    public Product updateProduct(Long id, Product product, MultipartFile imageFile) throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setGender(product.getGender());
        existingProduct.setQuantity(product.getQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingProduct.setImage(imagePath);
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IOException("File ảnh trống!");
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName;
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public void reduceProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + productId));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ số lượng!");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public Page<Product> getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }


}
