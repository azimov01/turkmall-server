package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value ="Select *,b.id,b.count from product p join basket b on p.id = b.product_id where b.user_id=:userId limit :size offset :size * :page", nativeQuery = true)
    List<Product> findAllByUserId(@Param("userId") UUID userId, @Param("page") Integer page, @Param("size") Integer size);

    @Query(nativeQuery = true,
            value = "SELECT * from product where trend=true like :size offset :size * :page"
    )
    List<Product> findAllByTrendTrue(@Param("page") Integer page, @Param("size") Integer size);

    @Query(nativeQuery = true,
            value = "SELECT count(*) from product where trend=true"
    )
    Integer countTrendProduct();

}
