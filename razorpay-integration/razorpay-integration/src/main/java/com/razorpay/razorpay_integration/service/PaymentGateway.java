package com.razorpay.razorpay_integration.service;


import com.razorpay.razorpay_integration.dto.PaymentLinkRequestDto;
import com.razorpay.razorpay_integration.models.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {
    String createPaymentLink(PaymentLinkRequestDto paymentLinkRequestDto);
    PaymentStatus getStatus(String paymentId, String orderId);
}
