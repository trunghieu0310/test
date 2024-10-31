package com.javaweb.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.DiemSinhVienDTO;
import com.javaweb.service.DiemSinhVienService;

@RestController
public class DiemSinhVienAPI {
    private static final Logger logger = LoggerFactory.getLogger(DiemSinhVienAPI.class);

    @Autowired 
    private DiemSinhVienService diemSinhVienService;

    @GetMapping(value = "/api/sinhvien/")
    public ResponseEntity<List<DiemSinhVienDTO>> getSinhVien(@RequestParam Map<String, Object> params) {
        List<DiemSinhVienDTO> result = diemSinhVienService.findSinhVien(params);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/sinhvien/")
    public ResponseEntity<List<DiemSinhVienDTO>> insertSinhVien(@RequestBody List<DiemSinhVienDTO> diemSinhVienDTOList) {
        try {
            for (DiemSinhVienDTO dto : diemSinhVienDTOList) {
                validateDiemSinhVienDTO(dto);
            }

            List<DiemSinhVienDTO> savedList = diemSinhVienService.saveAll(diemSinhVienDTOList);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
        } catch (IllegalArgumentException e) {
            logger.error("Lỗi hợp lệ khi thêm sinh viên: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm sinh viên: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cập nhật nhiều sinh viên
    @PutMapping(value = "/api/sinhvien/")
    public ResponseEntity<List<DiemSinhVienDTO>> updateSinhVien(@RequestBody List<DiemSinhVienDTO> diemSinhVienDTOList) {
        try {
            for (DiemSinhVienDTO dto : diemSinhVienDTOList) {
                validateDiemSinhVienDTO(dto);
            }

            List<DiemSinhVienDTO> updatedList = diemSinhVienService.updateAll(diemSinhVienDTOList);
            return ResponseEntity.ok(updatedList);
        } catch (IllegalArgumentException e) {
            logger.error("Lỗi hợp lệ khi cập nhật sinh viên: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật sinh viên: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping(value = "/api/sinhvien/")
    public ResponseEntity<String> deleteDiemSinhVien(
            @RequestParam String maSV,
            @RequestParam String maHP,
            @RequestParam(defaultValue = "false") boolean confirmDelete) {
        
        String responseMessage = diemSinhVienService.deleteDiemSinhVien(maSV, maHP, confirmDelete);
        return ResponseEntity.ok(responseMessage);
    }


    // Phương thức kiểm tra tính hợp lệ
    private void validateDiemSinhVienDTO(DiemSinhVienDTO dto) {
        List<String> errors = new ArrayList<>();
        int currentYear = 2024; // Điều chỉnh theo yêu cầu

        if (dto.getNamHoc() > currentYear) {
            errors.add("Năm học không được vượt quá " + currentYear);
        }

        if (dto.getDiemChuyenCan() < 0 || dto.getDiemChuyenCan() > 10) {
            errors.add("Điểm chuyên cần phải trong khoảng từ 0 đến 10.");
        }
        if (dto.getDiemKiemTra() < 0 || dto.getDiemKiemTra() > 10) {
            errors.add("Điểm kiểm tra phải trong khoảng từ 0 đến 10.");
        }
        if (dto.getDiemThaoLuan() < 0 || dto.getDiemThaoLuan() > 10) {
            errors.add("Điểm thảo luận phải trong khoảng từ 0 đến 10.");
        }
        if (dto.getDiemCuoiKy() < 0 || dto.getDiemCuoiKy() > 10) {
            errors.add("Điểm cuối kỳ phải trong khoảng từ 0 đến 10.");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}

