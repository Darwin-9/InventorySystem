package com.binarycode.InventorySystemBackend.dto;

public class Verify2FARequestDTO {
    private Long userId;
    private String code;

    public Verify2FARequestDTO() {}

    public Verify2FARequestDTO(Long userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
