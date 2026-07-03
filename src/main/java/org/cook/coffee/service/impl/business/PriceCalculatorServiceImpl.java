package org.cook.coffee.service.impl.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.entity.CoffeeSizeEntity;
import org.cook.coffee.model.business.OrderItemAdditiveCalculationResult;
import org.cook.coffee.model.business.OrderItemCalculationResult;
import org.cook.coffee.model.business.PriceCalculationResult;
import org.cook.coffee.model.order.CreateOrderItemAdditiveRequest;
import org.cook.coffee.model.order.CreateOrderItemRequest;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.repository.AdditiveRepository;
import org.cook.coffee.repository.CoffeeRepository;
import org.cook.coffee.repository.CoffeeSizeRepository;
import org.cook.coffee.service.interfaces.business.PriceCalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceCalculatorServiceImpl implements PriceCalculatorService {

    private final CoffeeRepository coffeeRepository;
    private final AdditiveRepository additiveRepository;
    private final CoffeeSizeRepository coffeeSizeRepository;

    @Override
    public PriceCalculationResult calculatePrice(CreateOrderRequest request) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItemCalculationResult> itemResults = new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : request.getItems()) {
            CoffeeEntity coffee = coffeeRepository.findById(itemRequest.getCoffeeId())
                    .orElseThrow(() -> new EntityNotFoundException("There is no coffee with id -> " + itemRequest.getCoffeeId()));

            CoffeeSizeEntity coffeeSize = coffeeSizeRepository.findById(itemRequest.getSizeId())
                    .orElseThrow(() -> new EntityNotFoundException("There is no coffee size with id -> " + itemRequest.getSizeId()));

            BigDecimal itemPrice = coffee.getPrice().add(coffeeSize.getPrice());

            List<OrderItemAdditiveCalculationResult> additiveResults = new ArrayList<>();

            if (itemRequest.getRequests() != null) {
                for (CreateOrderItemAdditiveRequest additiveRequest: itemRequest.getRequests()) {
                    AdditiveEntity additive = additiveRepository.findById(additiveRequest.getAdditiveId())
                            .orElseThrow(() -> new EntityNotFoundException("There is no additive with id -> " + additiveRequest.getAdditiveId()));

                    BigDecimal additivePrice = additive.getPrice().multiply(BigDecimal.valueOf(additiveRequest.getQuantity()));

                    itemPrice = itemPrice.add(additivePrice);

                    additiveResults.add(new OrderItemAdditiveCalculationResult(additiveRequest, additive, additiveRequest.getQuantity(), additivePrice));
                }
            }

            BigDecimal totalItemPrice = itemPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalPrice = totalPrice.add(totalItemPrice);

            itemResults.add(new OrderItemCalculationResult(itemRequest, coffee, coffeeSize, totalItemPrice, itemRequest.getQuantity(), additiveResults));
        }

        return new PriceCalculationResult(totalPrice, itemResults);
    }
}
