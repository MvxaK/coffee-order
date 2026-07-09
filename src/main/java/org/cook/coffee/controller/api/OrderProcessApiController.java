package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.model.order.Order;
import org.cook.coffee.service.interfaces.business.OrderProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderProcessApiController {

    private final OrderProcessService orderProcessService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request){
        Order order = orderProcessService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(order);
    }

    @PostMapping("/reserve-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> reserveStock(@RequestBody Long orderId){
        orderProcessService.reserveStock(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/commit-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> commitStock(@RequestBody Long orderId){
        orderProcessService.commitStock(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/release-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> releaseStock(@RequestBody Long orderId){
        orderProcessService.releaseStock(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/confirm-payment")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> confirmPayment(@RequestBody Long orderId){
        orderProcessService.confirmPayment(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/start-prepartion")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> startPreparation(@RequestBody Long orderId){
        orderProcessService.startPreparation(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/mark-ready")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN', 'ROLE_WORKER')")
    public ResponseEntity<Void> markReady(@RequestBody Long orderId){
        orderProcessService.markReady(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/complete-order")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN', 'ROLE_WORKER')")
    public ResponseEntity<Void> completeOrder(@RequestBody Long orderId){
        orderProcessService.completeOrder(orderId);

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/cancel-order")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> cancelOrder(@RequestBody Long orderId){
        orderProcessService.cancelOrder(orderId);

        return ResponseEntity.noContent()
                .build();
    }

}
