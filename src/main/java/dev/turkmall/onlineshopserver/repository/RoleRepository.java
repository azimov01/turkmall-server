package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Role;
import dev.turkmall.onlineshopserver.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
