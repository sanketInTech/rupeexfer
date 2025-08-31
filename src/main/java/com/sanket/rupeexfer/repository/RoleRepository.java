package com.sanket.rupeexfer.repository;

import com.sanket.rupeexfer.model.Role;
import com.sanket.rupeexfer.model.Role.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
