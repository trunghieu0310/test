package com.javaweb.service;

import java.util.List;
import java.util.Map;
import com.javaweb.model.DiemSinhVienDTO;

public interface DiemSinhVienService {
	public List<DiemSinhVienDTO> findSinhVien(Map<String,Object> params);
	List<DiemSinhVienDTO> saveAll(List<DiemSinhVienDTO> diemSinhVienDTOList);  // Thêm mới
	List<DiemSinhVienDTO> updateAll(List<DiemSinhVienDTO> diemSinhVienDTOList);
	String deleteDiemSinhVien(String maSV, String maHP, boolean confirmDelete);
}
