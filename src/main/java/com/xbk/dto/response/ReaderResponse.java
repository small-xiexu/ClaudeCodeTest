package com.xbk.dto.response;

import com.xbk.entity.Reader;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

/**
 * 读者响应
 */
@Schema(description = "读者响应")
public class ReaderResponse {

    @Schema(description = "读者ID", example = "1")
    private Integer id;

    @Schema(description = "借书证号", example = "R20240001")
    private String cardNumber;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "手机号", example = "13812345678")
    private String phone;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "地址", example = "北京市朝阳区XXX街道XXX号")
    private String address;

    @Schema(description = "最大借阅数量", example = "5")
    private Integer maxBorrowCount;

    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private Timestamp createdAt;

    @Schema(description = "更新时间", example = "2024-01-01 12:00:00")
    private Timestamp updatedAt;

    public ReaderResponse() {
    }

    public static ReaderResponse fromEntity(Reader reader) {
        if (reader == null) {
            return null;
        }
        ReaderResponse response = new ReaderResponse();
        response.setId(reader.getId());
        response.setCardNumber(reader.getCardNumber());
        response.setName(reader.getName());
        response.setGender(reader.getGender());
        response.setPhone(reader.getPhone());
        response.setEmail(reader.getEmail());
        response.setAddress(reader.getAddress());
        response.setMaxBorrowCount(reader.getMaxBorrowCount());
        response.setCreatedAt(reader.getCreatedAt());
        response.setUpdatedAt(reader.getUpdatedAt());
        return response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMaxBorrowCount() {
        return maxBorrowCount;
    }

    public void setMaxBorrowCount(Integer maxBorrowCount) {
        this.maxBorrowCount = maxBorrowCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
