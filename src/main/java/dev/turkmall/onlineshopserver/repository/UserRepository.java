package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String s);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE id IN\n" +
            "                          (SELECT user_id FROM user_role WHERE role_id = (SELECT id FROM role WHERE role_name = :roleName))")
    Page<User> findAllByRoleName(Pageable pageable, String roleName);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE id NOT IN\n" +
            "                          (SELECT user_id FROM user_role WHERE role_id = (SELECT id FROM role WHERE role_name = :roleName))")
    Page<User> findAll(Pageable pageable, String roleName);

    @Query(nativeQuery = true, value = "SELECT *\n" +
            "FROM users\n" +
            "WHERE (\n" +
            "        LOWER(users.phone_number) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.phone_number)\n" +
            "        OR LOWER(users.first_name) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.first_name)\n" +
            "        OR LOWER(users.last_name) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.last_name)\n" +
            "    )\n" +
            "  AND id NOT IN (SELECT user_id FROM user_role WHERE role_id = (SELECT id FROM role WHERE role_name = :roleName))")
    Page<User> searchWithOutAdmin(Pageable pageable, String search, String roleName);


    @Query(nativeQuery = true, value = "SELECT *\n" +
            "FROM users\n" +
            "WHERE (\n" +
            "        LOWER(users.phone_number) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.phone_number)\n" +
            "        OR LOWER(users.first_name) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.first_name)\n" +
            "        OR LOWER(users.last_name) LIKE :search\n" +
            "        OR :search LIKE LOWER(users.last_name)\n" +
            "    )\n" +
            "  AND id IN (SELECT user_id FROM user_role WHERE role_id = (SELECT id FROM role WHERE role_name = :roleName))")
    Page<User> search(Pageable pageable, String search, String roleName);

}
