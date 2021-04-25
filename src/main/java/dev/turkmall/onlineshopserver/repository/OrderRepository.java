package dev.turkmall.onlineshopserver.repository;

import dev.turkmall.onlineshopserver.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsBySerialNumber(String serialNumber);
}
