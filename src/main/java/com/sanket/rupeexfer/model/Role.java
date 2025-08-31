package com.sanket.rupeexfer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private ERole name;
    
    public enum ERole {
        ROLE_USER,
        ROLE_ADMIN
    }
}
