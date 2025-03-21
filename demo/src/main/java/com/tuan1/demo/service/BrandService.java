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

    // Inject repository để làm việc với cơ sở dữ liệu
    @Autowired
    private BrandRepository brandRepository;

    /**
     * Lấy danh sách tất cả thương hiệu trong database.
     * @return List<Brand> danh sách thương hiệu.
     */
    public List<Brand> getAllBrands() {
        return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    public void saveBrand(Brand brand) {
        brandRepository.save(brand);
    }



    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    /**
     * Lấy thông tin một thương hiệu theo ID.
     * @param id ID của thương hiệu cần tìm.
     * @return Brand nếu tìm thấy, nếu không trả về null.
     */
    public Brand getBrandById(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        return optionalBrand.orElse(null); // Trả về null nếu không tìm thấy
    }

    /**
     * Cập nhật thông tin một thương hiệu có sẵn trong database.
     * @param brand Đối tượng Brand đã chỉnh sửa cần cập nhật.
     */
    public void updateBrand(Brand brand) {
        brandRepository.save(brand); // save() hoạt động như update nếu ID đã tồn tại
    }
}
