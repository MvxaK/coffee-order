package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.orderResponse.OrderDetailsResponse;
import org.cook.coffee.model.orderResponse.OrderSummaryResponse;
import org.cook.coffee.security.config.CustomUserDetails;
import org.cook.coffee.service.interfaces.ProfileOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile/orders")
@RequiredArgsConstructor
public class ProfileApiController {

    private final ProfileOrderService profileOrderService;

    @GetMapping
    public List<OrderSummaryResponse> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return profileOrderService.getUserOrders(userDetails.getUuid());
    }

    @GetMapping("/{id}")
    public OrderDetailsResponse getOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return profileOrderService.getOrderDetails(id, userDetails.getUuid());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        profileOrderService.cancelOrder(id, userDetails.getUuid());

        return ResponseEntity.accepted()
                .build();
    }
}
