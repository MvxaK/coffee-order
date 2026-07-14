package org.cook.coffee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "additives")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditiveEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer reservedStock;
    private String url;
    private boolean deleted = false;

    @Version
    private Long version;

}
