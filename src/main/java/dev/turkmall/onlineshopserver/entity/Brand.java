package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.templates.AbsNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand extends AbsNameEntity {

    @OneToOne(optional = false,fetch = FetchType.LAZY)
    private Attachment attachment;
}
