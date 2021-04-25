package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Commentary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentaryRepository extends JpaRepository<Commentary, UUID> {

    Page<Commentary> findAllByReplyId(UUID subCommentary_id, Pageable pageable);
    Page<Commentary> findAllByProductId(UUID product_id, Pageable pageable);

}