package org.cook.coffee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeSize {

    private Long id;
    private String size;
    private BigDecimal price;

}
