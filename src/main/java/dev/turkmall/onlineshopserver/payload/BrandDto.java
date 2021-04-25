package dev.turkmall.onlineshopserver.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrandDto {

    private Integer id;
    private String nameUz;
    private String nameRu;
    private boolean active;
    private UUID attachmentId;
}
