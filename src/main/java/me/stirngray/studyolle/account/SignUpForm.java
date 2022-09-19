package me.stirngray.studyolle.account;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignUpForm {

    @Email
    @NotBlank
    private String email;


    @NotBlank
    @Max(8)@Min(50)
    private String password;


    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

}
