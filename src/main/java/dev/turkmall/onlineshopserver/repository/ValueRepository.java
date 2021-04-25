package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ValueRepository extends JpaRepository<Value, UUID> {
    List<Value> findAllByProductId(UUID product_id);

    void deleteAllByProductId(UUID product_id);
}
