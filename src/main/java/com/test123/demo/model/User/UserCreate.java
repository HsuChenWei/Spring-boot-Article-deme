package com.test123.demo.model.User;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class UserCreate {

    @Schema(description = "使用者名稱")
    private String userName;

    @Schema(description = "密碼")
    private String userPwd;

    @Schema(description = "信箱")
    private String email;

}
