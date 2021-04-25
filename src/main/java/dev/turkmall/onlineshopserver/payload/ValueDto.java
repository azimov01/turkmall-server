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
public class ValueDto {
    private UUID id;
    private String name;
    private Integer detailId;
    private String detailNameUz;
    private String detailNameRu;
}
