package com.test123.demo.service.Impl;

import com.test123.demo.entity.QUser;
import com.test123.demo.entity.Role;
import com.test123.demo.entity.User;
import com.test123.demo.entity.UserRole;
import com.test123.demo.model.Role.RoleDto;
import com.test123.demo.model.Security.TokenPair;
import com.test123.demo.model.User.UserCreate;
import com.test123.demo.model.User.UserLogin;
import com.test123.demo.model.User.UserRoleChange;
import com.test123.demo.model.User.UserUpdate;
import com.test123.demo.repository.RoleRepository;
import com.test123.demo.repository.UserRepository;
import com.test123.demo.repository.UserRoleRepository;
import com.test123.demo.service.Impl.querydsl.QuerydslRepository;
import com.test123.demo.service.UserService;
import com.test123.demo.service.utils.UtilService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private QuerydslRepository querydsl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilService utilService;
    @Override
    public List<User> getAllUserWithRole() {
        return userRepository.findAll();
    }

    @Override
    public Option<User> getUserById(String id) {
        QUser user = QUser.user;
        return Option.of(querydsl.newQuery()
                .selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    @Override
    @Transactional
    public Option<User> createUser(UserCreate creation) throws IOException {
        if (creation.getUserName() == null || creation.getUserName().trim().isEmpty()){
            throw new IllegalArgumentException("UserName can't be null or empty");
        }
        if (creation.getUserPwd() == null || creation.getUserPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (creation.getEmail() == null || creation.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (userRepository.existsByUserName(creation.getUserName())){
            throw new RuntimeException("This user already exist");
        }
        User user = new User();
        user.setUserName(creation.getUserName());
        String encryptPassword = passwordEncoder.encode(creation.getUserPwd());
        user.setUserPwd(encryptPassword);
        user.setEmail(creation.getEmail());

        User newUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(newUser);


        Role defaultRole = roleRepository.findById("0").orElseThrow(() -> new RuntimeException("Default role not found"));
        userRole.setRole(defaultRole);

        userRoleRepository.save(userRole);

        return Option.of(newUser);
    }

    @Override
    public Option<User> getUserByName(String userName) {
        QUser user = QUser.user;
        return Option.of(querydsl.newQuery()
                .selectFrom(user)
                .where(user.userName.eq(userName))
                .fetchOne());
    }



    @Override
    @Transactional
    public Option<TokenPair> userLogin(UserLogin userLogin) {
        Option<User> userOption = getUserByName(userLogin.getUserName());
        if (!userOption.isEmpty()){
            User user = userOption.get();
            if (checkPwd(userLogin, user)){
                return utilService.generateTokenPair(user.getId());
            }else {
                throw new RuntimeException("User not found!");
            }
        }
        return Option.none();
    }

    @Override
    public List<String> getRoleIdsForUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();

        return user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getId())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Option<User> updateUser(UserUpdate update) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No user is currently authenticated.");
        }

        String currentUserId = authentication.getName();

        Optional<User> user = userRepository.findById(currentUserId);
        if(!user.isPresent()){
            throw new RuntimeException("User not found.");
        }
        User userUpdate = user.get();
        String encoderPwd = passwordEncoder.encode(update.getUserPwd());
        userUpdate.setUserPwd(encoderPwd);
        userUpdate.setEmail(update.getEmail());
        LocalDateTime updateRightNow = LocalDateTime.now();
        userUpdate.setUpdateAt(updateRightNow);

        User newUpdatUser = userRepository.save(userUpdate);


        return Option.of(newUpdatUser);
    }

    @Override
    @Transactional
    public Option<User> changeRole(String userId, Role newRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()){
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                List<UserRole> userRoles = user.getUserRoles();

                for (UserRole userRole : userRoles) {
                    if (Objects.equals(userRole.getRole().getId(), "0") || Objects.equals(userRole.getRole().getId(), "1")) {
                        userRole.setRole(newRole);
                        userRoleRepository.save(userRole);
                    }else {
                        throw new RuntimeException("0 or 1");
                    }
                }
                userRepository.save(user);
                return Option.of(user);
            }
        }
        return Option.none();
    }



    @Override
    public void removeUser(String id) {
        User existUser = getUserById(id).getOrElseThrow(() -> new RuntimeException("User not found."));
        userRepository.delete(existUser);
    }

    private boolean checkPwd(UserLogin user, User dbUser){
        String inputPwd = user.getUserPwd();
        return passwordEncoder.matches(inputPwd, dbUser.getUserPwd());
    }
}
