package dev.turkmall.onlineshopserver.config;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.entity.enums.RoleName;
import dev.turkmall.onlineshopserver.repository.RoleRepository;
import dev.turkmall.onlineshopserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.datasource.initialization-mode}")
    private String task;


    @Override
    public void run(String... args) {
        if (task.equals("always")) {
            User user = new User(
                    "Admin",
                    "Admin",
                    "admin@admin.com",
                    "+123456789",
                    passwordEncoder.encode("root123"),
                    true,
                    true,
                    true,
                    true,
                    Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_ADMIN))
            );
            userRepository.save(user);

            User seller = new User(
                    "seller",
                    "seller",
                    "seller@seller.com",
                    "+123456788",
                    passwordEncoder.encode("root123"),
                    true,
                    true,
                    true,
                    true,
                    Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_SELLER))
            );
            userRepository.save(seller);
        }
    }
}
