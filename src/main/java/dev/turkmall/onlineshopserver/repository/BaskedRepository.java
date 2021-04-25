package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaskedRepository extends JpaRepository<Basket, UUID> {
    @Query(value = "SELECT * from basket where user_id=:userId like :size offset :size * :page",nativeQuery = true)
    List<Basket> findAllByUserId(Integer page, Integer size, UUID userId);

    void deleteByUserIdAndId(UUID user_id, UUID id);

    Optional<Basket> findByUserIdAndProductId(UUID user_id, UUID product_id);
}
