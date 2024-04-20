package com.example.GameStore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ResetPasswordRequest {
    private String emal;
    private String resetToken;
    private String newPassword;
}
