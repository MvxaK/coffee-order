package org.cook.coffee.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.service.interfaces.CamundaClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-processes")
@RequiredArgsConstructor
public class CamundaApiController {

    private final CamundaClientService camundaClientService;

    @PostMapping
    public ResponseEntity<?> startOrderProcess(@RequestBody CreateOrderRequest request){
        try {
            camundaClientService.startOrderProcess(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.accepted()
                .build();
    }
}