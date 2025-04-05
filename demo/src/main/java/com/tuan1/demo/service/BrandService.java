package com.tuan1.demo.service;

import com.tuan1.demo.model.Brand;
import com.tuan1.demo.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {


    @Autowired
    private BrandRepository brandRepository;


    public List<Brand> getAllBrands() {
        return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    public void saveBrand(Brand brand) {
        brandRepository.save(brand);
    }



    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }


    public Brand getBrandById(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        return optionalBrand.orElse(null); // Trả về null nếu không tìm thấy
    }


    public void updateBrand(Brand brand) {
        brandRepository.save(brand); // save() hoạt động như update nếu ID đã tồn tại
    }
}
