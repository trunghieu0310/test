package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.convert.Caculate;
import com.javaweb.model.DiemSinhVienDTO;
import com.javaweb.repository.DiemSinhVienRepository;
import com.javaweb.repository.HocPhanRepository;
import com.javaweb.repository.SinhVienRepository;
import com.javaweb.repository.entity.DiemSinhVienEntity;
import com.javaweb.repository.entity.HocPhanEntity;
import com.javaweb.repository.entity.SinhVienEntity;
import com.javaweb.service.DiemSinhVienService;

@Service
public class DiemSinhVienServiceImpl implements DiemSinhVienService {
    private static final Logger logger = LoggerFactory.getLogger(DiemSinhVienServiceImpl.class);

    @Autowired
    private DiemSinhVienRepository diemSinhVienRepository;

    @Autowired
    private HocPhanRepository hocPhanRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public List<DiemSinhVienDTO> saveAll(List<DiemSinhVienDTO> diemSinhVienDTOList) {
        List<DiemSinhVienEntity> entities = diemSinhVienDTOList.stream()
                                                               .map(this::convertToEntity)
                                                               .collect(Collectors.toList());
        try {
            // Gọi hàm tính điểm trước khi lưu
            entities.forEach(entity -> new Caculate().calculateScores(entity));
            
            List<DiemSinhVienEntity> savedEntities = diemSinhVienRepository.saveAll(entities);
            return savedEntities.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Lỗi khi lưu danh sách điểm sinh viên.", e);
            throw new RuntimeException("Không thể lưu danh sách điểm sinh viên.", e);
        }
    }

    @Override
    public List<DiemSinhVienDTO> updateAll(List<DiemSinhVienDTO> diemSinhVienDTOList) {
        List<DiemSinhVienEntity> updatedEntities = new ArrayList<>();
        
        for (DiemSinhVienDTO dto : diemSinhVienDTOList) {
            if (!diemSinhVienRepository.existsByMaSV(dto.getMaSV())) {
                logger.warn("Không tìm thấy sinh viên với mã: {}", dto.getMaSV());
                throw new IllegalArgumentException("Mã sinh viên không tồn tại: " + dto.getMaSV());
            }
            updatedEntities.add(convertToEntity(dto));
        }

        try {
            // Gọi hàm tính điểm trước khi lưu
            updatedEntities.forEach(entity -> new Caculate().calculateScores(entity));

            List<DiemSinhVienEntity> savedEntities = diemSinhVienRepository.saveAll(updatedEntities);
            return savedEntities.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật danh sách điểm sinh viên.", e);
            throw new RuntimeException("Không thể cập nhật danh sách điểm sinh viên.", e);
        }
    }
    @Override
    public String deleteDiemSinhVien(String maSV, String maHP, boolean confirmDelete) {
        boolean isDeleted = diemSinhVienRepository.deleteByMaSVAndMaHP(maSV, maHP, confirmDelete);
        
        if (isDeleted) {
            return "Xóa điểm thành công!";
        } else {
            return "Không thể xóa điểm. Kiểm tra thông tin và xác nhận xóa.";
        }
    }

    @Override
    public List<DiemSinhVienDTO> findSinhVien(Map<String, Object> params) {
        List<DiemSinhVienEntity> diemSinhVienEntities = diemSinhVienRepository.findSinhVien(params);
        return diemSinhVienEntities.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }

    private DiemSinhVienDTO mapEntityToDTO(DiemSinhVienEntity item) {
        DiemSinhVienDTO sinhVienDTO = convertToDTO(item);
        SinhVienEntity sinhVien = sinhVienRepository.findNameByMaSv(item.getMaSV());
        HocPhanEntity hocPhan = hocPhanRepository.findNameByMaHP(item.getMaHP());

        sinhVienDTO.setTenSV(sinhVien != null ? sinhVien.getTenSV() : null);
        if (hocPhan != null) {
            sinhVienDTO.setMaHP(item.getMaHP());
            sinhVienDTO.setTenMonHoc(hocPhan.getMonHoc());
            sinhVienDTO.setKyHoc(hocPhan.getHocKy());
            sinhVienDTO.setNamHoc(hocPhan.getNamHoc());
        }
        return sinhVienDTO;
    }

    private DiemSinhVienEntity convertToEntity(DiemSinhVienDTO dto) {
        DiemSinhVienEntity entity = new DiemSinhVienEntity();
        entity.setMaSV(dto.getMaSV());
        entity.setMaHP(dto.getMaHP());
        entity.setDiemChuyenCan(dto.getDiemChuyenCan());
        entity.setDiemKiemTra(dto.getDiemKiemTra());
        entity.setDiemThaoLuan(dto.getDiemThaoLuan());
        entity.setDiemCuoiKy(dto.getDiemCuoiKy());
        entity.setDiemTongKet(dto.getDiemTongKet());
        entity.setDiemHeSo4(dto.getDiemHeSo4());
        entity.setDiemHeChu(dto.getDiemHeChu());
        return entity;
    }

    private DiemSinhVienDTO convertToDTO(DiemSinhVienEntity entity) {
        DiemSinhVienDTO dto = new DiemSinhVienDTO();
        dto.setMaSV(entity.getMaSV());
        dto.setMaHP(entity.getMaHP());
        dto.setDiemChuyenCan(entity.getDiemChuyenCan());
        dto.setDiemKiemTra(entity.getDiemKiemTra());
        dto.setDiemThaoLuan(entity.getDiemThaoLuan());
        dto.setDiemCuoiKy(entity.getDiemCuoiKy());
        dto.setDiemTongKet(entity.getDiemTongKet());
        dto.setDiemHeSo4(entity.getDiemHeSo4());
        dto.setDiemHeChu(entity.getDiemHeChu());
        return dto;
    }
}

