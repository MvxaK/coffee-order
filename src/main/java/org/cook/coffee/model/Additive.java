package org.cook.coffee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Additive {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer reservedStock;
    private String url;
    private boolean deleted;

}
