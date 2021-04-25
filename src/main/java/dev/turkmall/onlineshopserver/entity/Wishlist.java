package dev.turkmall.onlineshopserver.entity;

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
@Table(uniqueConstraints ={@UniqueConstraint(columnNames = {"user_id","product_id"})})
public class Wishlist extends AbsEntity {
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Product product;
}
