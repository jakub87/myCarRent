package net.atos.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSearch {
    private String mark ="";
    private String model = "";
    private Integer yearFrom;
    private Integer yearTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String sorting;
    public String getSorting() {
        return Optional.ofNullable(sorting).orElse("car_id__asc");
    }

}
