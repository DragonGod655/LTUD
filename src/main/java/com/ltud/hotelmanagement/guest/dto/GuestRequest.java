package com.ltud.hotelmanagement.guest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class GuestRequest {

    @NotBlank(message = "Họ và tên khách hàng không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải từ 10 - 11 chữ số")
    private String phone;

    @Email(message = "Địa chỉ email không đúng định dạng")
    private String email;

    private String identityCard;

    public GuestRequest() {
    }

    public GuestRequest(String fullName, String phone, String email, String identityCard) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.identityCard = identityCard;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
