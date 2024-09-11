package com.razorpay.razorpay_integration.service;

import com.razorpay.razorpay_integration.dto.PaymentLinkRequestDto;
import com.razorpay.razorpay_integration.models.PaymentDetails;
import com.razorpay.razorpay_integration.models.PaymentStatus;
import com.razorpay.razorpay_integration.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service

public class PaymentService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentGateway paymentGateway, PaymentRepository paymentRepository) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
    }

    public String createLink(String orderId){

        PaymentLinkRequestDto paymentLinkRequestDto = new PaymentLinkRequestDto();
        paymentLinkRequestDto.setCustomerName("Saurabh");
        paymentLinkRequestDto.setOrderId(orderId);
        paymentLinkRequestDto.setPhone("9893916735");
        paymentLinkRequestDto.setAmount(10000);

        // Generate payment link using the payment gateway
        String paymentLink = paymentGateway.createPaymentLink(paymentLinkRequestDto);

        // Save payment details in the repository
        PaymentDetails paymentResponse = new PaymentDetails();
        paymentResponse.setPaymentLink(paymentLink);
        paymentResponse.setOrderId(orderId);
        paymentRepository.save(paymentResponse);

        return paymentLink;
    }

    public PaymentStatus getPaymentStatus(String paymentId, String orderId) {
        // Retrieve payment details by order ID
        Optional<PaymentDetails> paymentDetails = paymentRepository.findByOrderId(orderId);

        if(paymentDetails.isEmpty()){
            throw new RuntimeException("Payment not found");
        }

        // Get payment status from the payment gateway
        PaymentStatus status = paymentGateway.getStatus(paymentId, orderId);

        // Update and save payment details with the new status
        PaymentDetails paymentResponse = paymentDetails.get();
        paymentResponse.setStatus(status);
        paymentResponse.setPaymentId(paymentId);
        paymentResponse.setDate(new Date());
        paymentRepository.save(paymentResponse);
        return status;
    }
}

