package com.xbk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 读者更新请求
 */
@Schema(description = "读者更新请求")
public class ReaderUpdateRequest {

    @Schema(description = "借书证号", example = "R20240001")
    @Pattern(regexp = "^R\\d{8}$", message = "借书证号格式不正确，正确格式：R加8位数字")
    private String cardNumber;

    @Schema(description = "姓名", example = "张三")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "性别", example = "男")
    @Pattern(regexp = "^[男女]$", message = "性别只能为'男'或'女'")
    private String gender;

    @Schema(description = "手机号", example = "13812345678")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "地址", example = "北京市朝阳区XXX街道XXX号")
    @Size(max = 100, message = "地址长度不能超过100个字符")
    private String address;

    public ReaderUpdateRequest() {
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
}
