package org.cook.coffee.entity.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.entity.AdditiveEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item_additives")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditiveEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItemEntity orderItem;

    @ManyToOne
    @JoinColumn(name = "additive_id")
    private AdditiveEntity additive;

    private String additiveName;
    private BigDecimal price;
    private Integer quantity;

}
