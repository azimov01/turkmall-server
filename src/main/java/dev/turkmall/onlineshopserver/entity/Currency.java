package dev.turkmall.onlineshopserver.entity;

import dev.turkmall.onlineshopserver.entity.templates.AbsNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Currency extends AbsNameEntity {

}
