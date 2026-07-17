package org.cook.coffee.entity.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.entity.CoffeeSizeEntity;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "coffee_id")
    private CoffeeEntity coffee;

    @ManyToOne
    @JoinColumn(name = "coffee_size_id")
    private CoffeeSizeEntity coffeeSize;

    private String coffeeName;
    private String sizeName;
    private int quantity;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemAdditiveEntity> additives;

    private BigDecimal price;

}
