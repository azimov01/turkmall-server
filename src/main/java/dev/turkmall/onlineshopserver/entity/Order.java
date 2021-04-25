package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.enums.OrderStatus;
import dev.turkmall.onlineshopserver.entity.templates.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends AbsEntity {
    @Column(nullable = false,unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private User user;
}
