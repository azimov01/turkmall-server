package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Detail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<Detail, Integer> {

    Page<Detail> findAllByActive(boolean active, Pageable pageable);
    Page<Detail> findAllByCategoryId(Integer category_id, Pageable pageable);
    Page<Detail> findAllByActiveAndCategoryId(boolean active, Integer category_id, Pageable pageable);
}
