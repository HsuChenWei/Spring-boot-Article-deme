package com.test123.demo.model.User;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserList {


    @Schema(description = "使用者ID")
    private String id;

    @Schema(description = "帳號")
    private String userName;

    @Schema(description = "信箱")
    private String email;

    @Schema(description = "創建時間")
    private LocalDateTime createAt;

    @Schema(description = "更新時間")
    private LocalDateTime updateAt;

    @Schema(description = "狀態")
    private String status;

//    private String userRole;


}
