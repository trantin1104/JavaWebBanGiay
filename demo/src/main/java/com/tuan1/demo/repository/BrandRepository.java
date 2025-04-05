package com.tuan1.demo.repository;

import com.tuan1.demo.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    // JpaRepository đã cung cấp sẵn các phương thức CRUD, không cần viết thêm gì ở đây
}
