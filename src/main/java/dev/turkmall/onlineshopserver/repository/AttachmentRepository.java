package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
