package com.test123.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "role_name")
    private String roleName;

//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserRole> userRoles = new ArrayList<>();

}
