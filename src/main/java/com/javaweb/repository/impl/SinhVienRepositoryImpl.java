package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.SinhVienRepository;
import com.javaweb.repository.entity.DiemSinhVienEntity;
import com.javaweb.repository.entity.SinhVienEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
@Repository
public class SinhVienRepositoryImpl implements SinhVienRepository{

	@Override
	public SinhVienEntity findNameByMaSv(String MaSV) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT * FROM sinhvien");
		StringBuilder where = new StringBuilder(" WHERE 1 = 1");
		SinhVienEntity sinhVienEntity = new SinhVienEntity();
		try(Connection conn = ConnectionJDBCUtil.getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			while(rs.next()) {
				sinhVienEntity.setMaSV(rs.getString("MaSV"));
				sinhVienEntity.setTenSV(rs.getString("TenSV"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("connect fasle");
		}
		return sinhVienEntity;
	}

}
