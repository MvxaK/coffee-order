package org.cook.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.model.CoffeeSize;

import java.math.BigDecimal;

@Entity
@Table(name = "coffee_sizes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeSizeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private BigDecimal price;

}
