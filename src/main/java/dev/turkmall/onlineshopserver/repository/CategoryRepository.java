package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(nativeQuery = true, value = "SELECT * from category where category_id is null and active =:active")
    Page<Category> findAllFatherCategory(Pageable pageable, boolean active);
    @Query(nativeQuery = true, value = "SELECT * from category where category_id is null")
    Page<Category> findAllFatherCategory(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * from category where category_id =:id and active =:active")
    Page<Category> findAllChildCategory(Integer id, boolean active, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * from category where category_id =:id")
    Page<Category> findAllChildCategory(Integer id, Pageable pageable);

}
