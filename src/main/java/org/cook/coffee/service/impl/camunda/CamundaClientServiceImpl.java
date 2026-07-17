package org.cook.coffee.service.impl.camunda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.service.interfaces.camunda.CamundaClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaClientServiceImpl implements CamundaClientService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${camunda.url}")
    private String camundaUrl;

    @Value("${camunda.order-process-key}")
    private String processKey;

    @Override
    public void startOrderProcess(CreateOrderRequest request) throws JsonProcessingException {
        Map<String, Object> processVariables = new HashMap<>();

        processVariables.put("createOrderRequest",
                Map.of(
                        "value", objectMapper.writeValueAsString(request),
                        "type", "String"
                )
        );
        Map<String, Object> body = Map.of("variables", processVariables);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                camundaUrl + "/process-definition/key/" + processKey + "/start",
                body,
                Map.class
        );

        Map<String, Object> process = response.getBody();

        String processInstanceId = (String) process.get("id");
    }

    @Override
    public void cancelOrderProcess(Long orderId) {
        Map<String, Object> processVariables = new HashMap<>();

        processVariables.put("orderId",
                Map.of(
                        "value", orderId,
                        "type", "Integer"
                )
        );

        Map<String, Object> body = Map.of(
                "messageName", "OrderCancelled",
                "processInstanceVariables", processVariables
        );

        restTemplate.postForEntity(
                camundaUrl + "/message",
                body,
                Void.class
        );
    }

}