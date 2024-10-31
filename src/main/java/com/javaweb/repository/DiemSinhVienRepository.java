package com.javaweb.repository;

import java.util.List;
import java.util.Map;

import com.javaweb.repository.entity.DiemSinhVienEntity;
public interface DiemSinhVienRepository {
    List<DiemSinhVienEntity> findSinhVien(Map<String, Object> params);
    List<DiemSinhVienEntity> saveAll(List<DiemSinhVienEntity> diemSinhVienEntities); // Thêm phương thức này
    DiemSinhVienEntity update(DiemSinhVienEntity diemSinhVienEntity);
    boolean deleteByMaSVAndMaHP(String maSV, String maHP, boolean confirmDelete);
    boolean existsByMaSV(String maSV);
    boolean existsByMaHP(String maHP);
}

