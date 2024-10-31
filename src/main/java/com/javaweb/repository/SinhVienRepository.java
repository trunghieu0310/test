package com.javaweb.repository;

import com.javaweb.repository.entity.SinhVienEntity;

public interface SinhVienRepository {
	SinhVienEntity findNameByMaSv(String MaSV);
}
