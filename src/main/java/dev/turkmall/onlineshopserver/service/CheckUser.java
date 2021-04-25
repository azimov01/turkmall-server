package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.entity.Role;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.entity.enums.RoleName;
import org.springframework.stereotype.Service;

@Service
public class CheckUser {

    public boolean isAdmin(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName().equals(RoleName.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
