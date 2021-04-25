package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.templates.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    private boolean sale;

    private double salePrice;

    private Integer leftOver;

    private Integer viewCount;

    private boolean trend;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Attachment mainAttachment;

    @OneToMany
    private List<Attachment> attachmentList;
}
