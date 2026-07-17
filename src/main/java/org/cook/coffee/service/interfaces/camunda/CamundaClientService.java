package org.cook.coffee.service.interfaces.camunda;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface CamundaClientService {

    void startOrderProcess(CreateOrderRequest request) throws JsonProcessingException;
    void cancelOrderProcess(Long orderId);

}
