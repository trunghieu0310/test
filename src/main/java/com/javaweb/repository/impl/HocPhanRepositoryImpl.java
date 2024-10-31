package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.HocPhanRepository;
import com.javaweb.repository.entity.DiemSinhVienEntity;
import com.javaweb.repository.entity.HocPhanEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
@Repository
public class HocPhanRepositoryImpl implements HocPhanRepository{

	@Override
	public HocPhanEntity findNameByMaHP(String MaHP) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT * FROM lophocphan");
		StringBuilder where = new StringBuilder(" WHERE 1 = 1");
		HocPhanEntity result = new HocPhanEntity();
		try(Connection conn = ConnectionJDBCUtil.getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			while(rs.next()) {
				result.setMaHP(rs.getString("MaLHP"));
				result.setMonHoc(rs.getString("MonHoc"));
				result.setHocKy(rs.getLong("HocKy"));
				result.setNamHoc(rs.getLong("NamHoc"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("connect fasle");
		}
		return result;
	}

}
