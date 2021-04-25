package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.templates.AbsNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends AbsNameEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
