package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Product;
import dev.turkmall.onlineshopserver.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    @Query(value ="Select *, w.id from product p join wishlist w on p.id = w.product_id where w.user_id=:userId limit :size offset :size * :page", nativeQuery = true)
    List<Product> findAllByUserId(@Param("userId") UUID userId, @Param("page") Integer page, @Param("size") Integer size);

    void deleteByUserIdAndId(UUID user_id, UUID id);
}
