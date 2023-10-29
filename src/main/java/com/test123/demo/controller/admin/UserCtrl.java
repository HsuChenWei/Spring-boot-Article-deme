package com.test123.demo.controller.admin;

import com.test123.demo.entity.Role;
import com.test123.demo.model.Role.RoleDto;
import com.test123.demo.model.Security.TokenPair;
import com.test123.demo.model.User.*;
import com.test123.demo.model.wrapper.RespWrapper;
import com.test123.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Option;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin/user")
@Tag(name = "User", description = "會員功能")
public class UserCtrl {


    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;



    @Operation(summary = "取得所有會員資料")
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public RespWrapper<List<UserList>> getAllUser(){
        return RespWrapper.success(userService.getAllUserWithRole()
                .stream()
                .map(user -> modelMapper.map(user, UserList.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "會員註冊")
    @PostMapping("/register")
    public RespWrapper<UserDto> register(@Valid @RequestBody UserCreate creation) throws IOException {
        return  userService.createUser(creation)
                .map(user -> RespWrapper.success(modelMapper.map(user, UserDto.class))).get();
    }

    @Operation(summary = "取得JWT token驗證")
    @PostMapping("/login")
    public RespWrapper<TokenPair> login(@RequestBody UserLogin body) {
        Option<TokenPair> userOption = userService.userLogin(body);
        return userOption
                .map(u -> modelMapper.map(u ,TokenPair.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("UserName or PassWord is incorrect."));
    }

    @Operation(summary = "更新密碼, 信箱")
    @PutMapping("/update")
    public RespWrapper<UserDto> updateUser(@RequestBody UserUpdate body){


        return userService.updateUser(body)
                .map(user -> modelMapper.map(user, UserDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new RuntimeException("Update failed."));
    }

    @Operation(summary = "刪除會員")
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/delete/{id}")
    public RespWrapper<Void> removeUser (@PathVariable @Parameter(description = "使用者ID", required = false) String id){
        userService.removeUser(id);
        return RespWrapper.success(null);
    }

    @Operation(summary = "更改會員角色")
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/changeRole/{userId}")
    public RespWrapper<UserDto> changeRole(@PathVariable String userId, Role body){
        return userService.changeRole(userId, body)
                .map(user -> RespWrapper.success(modelMapper.map(user, UserDto.class)))
                .getOrElseThrow(() -> new RuntimeException("Failed to change role."));
    }


}
