package com.javaweb.repository;

import com.javaweb.repository.entity.HocPhanEntity;

public interface HocPhanRepository {
	HocPhanEntity findNameByMaHP(String MaHP);
}
