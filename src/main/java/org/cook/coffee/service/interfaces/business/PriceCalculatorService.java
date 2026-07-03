package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.model.business.PriceCalculationResult;
import org.cook.coffee.model.order.CreateOrderItemRequest;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PriceCalculatorService {

    PriceCalculationResult calculatePrice(CreateOrderRequest request);

}
