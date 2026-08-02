package com.ltud.hotelmanagement.guest.dto;

import com.ltud.hotelmanagement.guest.Guest;

import java.time.LocalDateTime;

public class GuestResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String identityCard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GuestResponse() {
    }

    public GuestResponse(Long id, String fullName, String phone, String email, String identityCard, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.identityCard = identityCard;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GuestResponse fromEntity(Guest guest) {
        if (guest == null) return null;
        return new Builder()
                .id(guest.getId())
                .fullName(guest.getFullName())
                .phone(guest.getPhone())
                .email(guest.getEmail())
                .identityCard(guest.getIdentityCard())
                .createdAt(guest.getCreatedAt())
                .updatedAt(guest.getUpdatedAt())
                .build();
    }

    public static class Builder {
        private Long id;
        private String fullName;
        private String phone;
        private String email;
        private String identityCard;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder identityCard(String identityCard) {
            this.identityCard = identityCard;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public GuestResponse build() {
            return new GuestResponse(id, fullName, phone, email, identityCard, createdAt, updatedAt);
        }
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
