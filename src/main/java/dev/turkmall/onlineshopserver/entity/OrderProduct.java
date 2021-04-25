package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.templates.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderProduct extends AbsEntity {
    @Column(nullable = false)
    private Integer countProduct;

    @Column(nullable = false)
    private Double productPrice;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Product product;
}
