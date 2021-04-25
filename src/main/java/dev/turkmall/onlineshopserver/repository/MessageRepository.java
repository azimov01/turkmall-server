package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findAllByRead(boolean read, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM message WHERE read=:read")
    Long countAllByRead(boolean read);
}
