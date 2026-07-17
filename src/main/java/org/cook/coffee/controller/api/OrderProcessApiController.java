package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.order.OrderIdRequest;
import org.cook.coffee.model.orderResponse.OrderStateResponse;
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
@RequestMapping("/api/order-processes")
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
    public ResponseEntity<Void> reserveStock(@RequestBody OrderIdRequest request){
        orderProcessService.reserveStock(request.getOrderId());

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/commit-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> commitStock(@RequestBody OrderIdRequest request){
        orderProcessService.commitStock(request.getOrderId());

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/release-stock")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> releaseStock(@RequestBody OrderIdRequest request){
        orderProcessService.releaseStock(request.getOrderId());

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/confirm-payment")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStateResponse> confirmPayment(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.confirmPayment(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/start-preparation")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStateResponse> startPreparation(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.startPreparation(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/mark-ready")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN', 'ROLE_WORKER')")
    public ResponseEntity<OrderStateResponse> markReady(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.markReady(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/complete-order")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN', 'ROLE_WORKER')")
    public ResponseEntity<OrderStateResponse> completeOrder(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.completeOrder(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/cancel-order")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStateResponse> cancelOrder(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.cancelOrder(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/refund-order")
    @PreAuthorize("hasAnyAuthority('ROLE_CAMUNDA', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStateResponse> refundOrder(@RequestBody OrderIdRequest request){
        OrderStateResponse response = orderProcessService.refundOrder(request.getOrderId());

        return ResponseEntity.ok()
                .body(response);
    }
}
