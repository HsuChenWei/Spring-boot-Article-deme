package com.test123.demo.service;

import com.test123.demo.entity.Role;
import com.test123.demo.entity.User;
import com.test123.demo.model.Role.RoleDto;
import com.test123.demo.model.Security.TokenPair;
import com.test123.demo.model.User.UserCreate;
import com.test123.demo.model.User.UserLogin;
import com.test123.demo.model.User.UserRoleChange;
import com.test123.demo.model.User.UserUpdate;
import io.vavr.control.Option;

import java.io.IOException;
import java.util.List;

public interface UserService {


    List<User> getAllUserWithRole();

    Option<User> getUserById(String id);

    Option<User> createUser(UserCreate creation) throws IOException;

    Option<User> getUserByName(String userName);

    Option<TokenPair> userLogin(UserLogin userLogin);

    List<String> getRoleIdsForUser(String userId);

    Option<User> updateUser(UserUpdate update);

    Option<User> changeRole(String userId, Role body);

    void removeUser(String id);

}
