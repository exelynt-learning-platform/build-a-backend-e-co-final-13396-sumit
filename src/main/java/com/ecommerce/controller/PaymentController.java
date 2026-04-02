package com.ecommerce.controller;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-session")
    public ResponseEntity<PaymentResponse> createPaymentSession(@RequestBody PaymentRequest paymentRequest) throws StripeException {
        PaymentResponse paymentResponse = paymentService.createPaymentSession(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}