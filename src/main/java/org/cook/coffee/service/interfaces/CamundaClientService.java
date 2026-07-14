package org.cook.coffee.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface CamundaClientService {

    void startOrderProcess(CreateOrderRequest request) throws JsonProcessingException;

}
