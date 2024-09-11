package com.razorpay.razorpay_integration.models;

import com.razorpay.Payment;
import lombok.Data;
@Data
public class PaymentResponse {
    private PaymentStatus status;
    private String mode;
    private String paymentId;
    private String orderId;
}
