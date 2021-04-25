package dev.turkmall.onlineshopserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    private Integer id;
    private String nameUz;
    private String nameRu;
    private boolean active;

    private List<String> names;

    private Integer categoryId;

    public DetailDto(Integer id, String nameUz, String nameRu, boolean active, Integer categoryId) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.active = active;
        this.categoryId = categoryId;
    }

}
