
package com.javaweb.convert;

    import com.javaweb.model.DiemSinhVienDTO;
import com.javaweb.repository.entity.DiemSinhVienEntity;

    public class Caculate {
        public void calculateScores(DiemSinhVienEntity data) {
        	 // In ra các điểm để kiểm tra
            System.out.println("Chuyen Can: " + data.getDiemChuyenCan());
            System.out.println("Kiem Tra: " + data.getDiemKiemTra());
            System.out.println("Thao Luan: " + data.getDiemThaoLuan());
            System.out.println("Cuoi Ky: " + data.getDiemCuoiKy());
            
            // Tính điểm tổng kết
            Double diemTongKet = data.getDiemChuyenCan()*0.1 + data.getDiemKiemTra()*0.15 + data.getDiemThaoLuan()*0.15 + data.getDiemCuoiKy()*0.6;
            data.setDiemTongKet(diemTongKet);

            data.setDiemHeSo4(convertToGPA(diemTongKet));
            data.setDiemHeChu(convertToLetterGrade(diemTongKet));
        }

        private Double convertToGPA(Double diemTongKet) {
            if (diemTongKet >= 9) return 4.0;
            else if (diemTongKet >= 8) return 3.5;
            else if (diemTongKet >= 7) return 3.0;
            else if (diemTongKet >= 6) return 2.5;
            else if (diemTongKet >= 5) return 2.0;
            else if (diemTongKet >= 4) return 1.0;
            else return 0.0;
        }

        private String convertToLetterGrade(Double diemTongKet) {
            if (diemTongKet >= 8.5) return "A";
            else if (diemTongKet >= 8) return "B+";
            else if (diemTongKet >= 7) return "B";
            else if (diemTongKet >= 6.5) return "C+";
            else if (diemTongKet >= 5) return "C";
            else if (diemTongKet >= 4) return "D";
            else return "F";
        }
    }

