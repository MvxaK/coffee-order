package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.orderResponse.OrderDetailsResponse;
import org.cook.coffee.service.interfaces.ProfileOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final ProfileOrderService profileOrderService;

    @GetMapping("/{userId}/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public OrderDetailsResponse getOrder(@PathVariable UUID userId, @PathVariable Long orderId){
        return profileOrderService.getOrderDetails(orderId, userId);
    }
}
