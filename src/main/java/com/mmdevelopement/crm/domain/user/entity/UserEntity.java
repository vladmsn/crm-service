package com.mmdevelopement.crm.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_KC_guid", nullable = false, unique = true, updatable = false)
    private String userGuid;

    @Column(name = "email")
    private String email;

    @Column(name = "roles")
    private String roles;

    @Column(name = "status")
    private String status;
}
