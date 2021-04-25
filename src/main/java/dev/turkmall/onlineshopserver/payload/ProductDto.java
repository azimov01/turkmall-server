package dev.turkmall.onlineshopserver.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private UUID id;

    private List<UUID> ids;

    private int count;

    private String name;

    private Double price;

    private boolean sale;

    private double salePrice;

    private Integer leftOver;

    private Integer viewCount;

    private boolean trend;

    private String serialNumber;

    private Integer categoryId;
    private String categoryNameUz;
    private String categoryNameRu;

    private UUID photoId;

    private List<UUID> photoIdList;

    private List<ValueDto> values;

    public ProductDto(UUID id, String name, Double price, boolean sale, double salePrice, Integer leftOver, Integer viewCount, boolean trend, String serialNumber, Integer categoryId, String categoryNameUz, String categoryNameRu, UUID photoId, List<UUID> photoIdList, List<ValueDto> values) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.salePrice = salePrice;
        this.leftOver = leftOver;
        this.viewCount = viewCount;
        this.trend = trend;
        this.serialNumber = serialNumber;
        this.categoryId = categoryId;
        this.categoryNameUz = categoryNameUz;
        this.categoryNameRu = categoryNameRu;
        this.photoId = photoId;
        this.photoIdList = photoIdList;
        this.values = values;
    }
}
