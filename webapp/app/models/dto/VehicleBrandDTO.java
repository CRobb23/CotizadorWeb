package models.dto;

import java.math.BigDecimal;

public class VehicleBrandDTO {
    private String brand;
    private String line;
    private String classCode;
    private Integer insurable;
    private String year;
    private BigDecimal value;

    public VehicleBrandDTO(String brand, String line, String classCode, Integer insurable) {
        this.brand = brand;
        this.line = line;
        this.classCode = classCode;
        this.insurable = insurable;
    }

    public VehicleBrandDTO(String brand, String line, String year, BigDecimal value) {
        this.brand = brand;
        this.line = line;
        this.year = year;
        this.value = value;
    }

    public String getBrand() {
        return brand;
    }

    public String getLine() {
        return line;
    }

    public String getClassCode() {
        return classCode;
    }

    public Integer getInsurable() {
        return insurable;
    }

    public String getYear() {
        return year;
    }

    public BigDecimal getValue() {
        return value;
    }
}
