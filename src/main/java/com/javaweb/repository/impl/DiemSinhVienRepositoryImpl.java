package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.javaweb.convert.Caculate;
import com.javaweb.model.DiemSinhVienDTO;
import com.javaweb.repository.DiemSinhVienRepository;
import com.javaweb.repository.entity.DiemSinhVienEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class DiemSinhVienRepositoryImpl implements DiemSinhVienRepository {

    private static void joinTable(Map<String, Object> params, StringBuilder sql) {
        if (StringUtil.checkString((String) params.get("TenSV"))) {
            sql.append(" INNER JOIN sinhvien sv ON ds.MaSV = sv.MaSV ");
        }
        if (StringUtil.checkString((String) params.get("MonHoc")) ||
            StringUtil.checkString((String) params.get("HocKy")) ||
            StringUtil.checkString((String) params.get("NamHoc"))) {
            sql.append(" INNER JOIN lophocphan hp ON ds.MaHP = hp.MaLHP ");
        }
    }

    private static void queryNormal(Map<String, Object> params, StringBuilder where) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!entry.getKey().equals("TenSV") && !entry.getKey().equals("HocKy") &&
                !entry.getKey().equals("NamHoc") && !entry.getKey().equals("MonHoc")) {
                String value = entry.getValue().toString();
                if (StringUtil.checkString(value)) {
                    if (NumberUtil.isNumber(value)) {
                        where.append(" AND ds." + entry.getKey() + "=" + value);
                    } else {
                        where.append(" AND ds." + entry.getKey() + " LIKE '%" + value + "%' ");
                    }
                }
            }
        }
    }

    private static void querySpecial(Map<String, Object> params, StringBuilder where) {
        if (StringUtil.checkString((String) params.get("TenSV"))) {
            where.append(" AND sv.TenSV LIKE '%" + params.get("TenSV") + "%' ");
        }
        if (StringUtil.checkString((String) params.get("MonHoc"))) {
            where.append(" AND hp.MonHoc LIKE '%" + params.get("MonHoc") + "%' ");
        }
        if (StringUtil.checkString((String) params.get("HocKy"))) {
            where.append(" AND hp.HocKy = " + params.get("HocKy"));
        }
        if (StringUtil.checkString((String) params.get("NamHoc"))) {
            where.append(" AND hp.NamHoc = " + params.get("NamHoc"));
        }
    }

    @Override
    public List<DiemSinhVienEntity> findSinhVien(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("SELECT ds.MaSV, ds.MaHP, ds.DiemChuyenCan, ds.DiemKiemTra, ds.DiemThaoLuan, ds.DiemCuoiKy, ds.DiemTongKet, ds.DiemHeSo4, ds.DiemHeChu FROM DiemSinhVien ds");
        joinTable(params, sql);
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        queryNormal(params, where);
        querySpecial(params, where);
        sql.append(where);

        List<DiemSinhVienEntity> result = new ArrayList<>();
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            while (rs.next()) {
                DiemSinhVienEntity diemSinhVien = new DiemSinhVienEntity();
                diemSinhVien.setMaSV(rs.getString("ds.MaSV"));
                diemSinhVien.setMaHP(rs.getString("ds.MaHP"));
                diemSinhVien.setDiemChuyenCan(rs.getDouble("ds.DiemChuyenCan"));
                diemSinhVien.setDiemKiemTra(rs.getDouble("ds.DiemKiemTra"));
                diemSinhVien.setDiemThaoLuan(rs.getDouble("ds.DiemThaoLuan"));
                diemSinhVien.setDiemCuoiKy(rs.getDouble("ds.DiemCuoiKy"));
                diemSinhVien.setDiemTongKet(rs.getDouble("ds.DiemTongKet"));
                diemSinhVien.setDiemHeSo4(rs.getDouble("ds.DiemHeSo4"));
                diemSinhVien.setDiemHeChu(rs.getString("ds.DiemHeChu"));
                result.add(diemSinhVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connect failed: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean existsByMaSV(String maSV) {
        String sql = "SELECT COUNT(*) FROM DiemSinhVien WHERE MaSV = ?";
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean existsByMaHP(String maHP) {
        String sql = "SELECT COUNT(*) FROM lophocphan WHERE MaLHP = ?";
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHP);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
 // Thêm phương thức kiểm tra sự tồn tại của cặp MaSV và MaHP
    public boolean existsByMaSVAndMaHP(String maSV, String maHP) {
        String sql = "SELECT COUNT(*) FROM DiemSinhVien WHERE MaSV = ? AND MaHP = ?";
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSV);
            pstmt.setString(2, maHP);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public DiemSinhVienEntity update(DiemSinhVienEntity diemSinhVienEntity) {
        // Kiểm tra sự tồn tại trước khi cập nhật theo cả MaSV và MaHP
        if (!existsByMaSVAndMaHP(diemSinhVienEntity.getMaSV(), diemSinhVienEntity.getMaHP())) {
            System.out.println("Bản ghi không tồn tại: " + diemSinhVienEntity.getMaSV() + " - " + diemSinhVienEntity.getMaHP());
            return null; 
        }

        String sql = "UPDATE DiemSinhVien SET DiemChuyenCan = ?, DiemKiemTra = ?, DiemThaoLuan = ?, DiemCuoiKy = ?, DiemTongKet = ?, DiemHeSo4 = ?, DiemHeChu = ? WHERE MaSV = ? AND MaHP = ?";
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, diemSinhVienEntity.getDiemChuyenCan());
            pstmt.setDouble(2, diemSinhVienEntity.getDiemKiemTra());
            pstmt.setDouble(3, diemSinhVienEntity.getDiemThaoLuan());
            pstmt.setDouble(4, diemSinhVienEntity.getDiemCuoiKy());
            pstmt.setDouble(5, diemSinhVienEntity.getDiemTongKet());
            pstmt.setDouble(6, diemSinhVienEntity.getDiemHeSo4());
            pstmt.setString(7, diemSinhVienEntity.getDiemHeChu());
            pstmt.setString(8, diemSinhVienEntity.getMaSV());
            pstmt.setString(9, diemSinhVienEntity.getMaHP());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return diemSinhVienEntity; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update failed: " + e.getMessage());
        }
        return null; 
    }

    @Override
    public List<DiemSinhVienEntity> saveAll(List<DiemSinhVienEntity> diemSinhVienEntities) {
        String sql = "INSERT INTO DiemSinhVien (MaSV, MaHP, DiemChuyenCan, DiemKiemTra, DiemThaoLuan, DiemCuoiKy, DiemTongKet, DiemHeSo4, DiemHeChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<DiemSinhVienEntity> result = new ArrayList<>();

        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Bắt đầu transaction

            for (DiemSinhVienEntity entity : diemSinhVienEntities) {
                if (existsByMaSVAndMaHP(entity.getMaSV(), entity.getMaHP())) {
                    update(entity);
                } else {
                    new Caculate().calculateScores(entity);
                    pstmt.setString(1, entity.getMaSV());
                    pstmt.setString(2, entity.getMaHP());
                    pstmt.setDouble(3, entity.getDiemChuyenCan());
                    pstmt.setDouble(4, entity.getDiemKiemTra());
                    pstmt.setDouble(5, entity.getDiemThaoLuan());
                    pstmt.setDouble(6, entity.getDiemCuoiKy());
                    pstmt.setDouble(7, entity.getDiemTongKet());
                    pstmt.setDouble(8, entity.getDiemHeSo4());
                    pstmt.setString(9, entity.getDiemHeChu());
                    pstmt.addBatch();
                    
                    result.add(entity);
                }
            }
            
            pstmt.executeBatch();
            conn.commit(); // Kết thúc transaction

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Batch insert failed: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean deleteByMaSVAndMaHP(String maSV, String maHP, boolean confirmDelete) {
        if (!confirmDelete) {
            return false; // Nếu không xác nhận xóa, trả về false
        }
        
        // Kiểm tra sự tồn tại của bản ghi
        if (!existsByMaSVAndMaHP(maSV, maHP)) {
            System.out.println("Bản ghi không tồn tại: " + maSV + " - " + maHP);
            return false; // Nếu bản ghi không tồn tại, trả về false
        }

        String sql = "DELETE FROM DiemSinhVien WHERE MaSV = ? AND MaHP = ?";
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSV);
            pstmt.setString(2, maHP);
            int rowsAffected = pstmt.executeUpdate(); // Thực hiện xóa
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Xóa thất bại: " + e.getMessage());
        }
        return false; // Nếu có lỗi xảy ra, trả về false
    }


}

