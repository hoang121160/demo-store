package com.example.GameStore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
