package com.razorpay.razorpay_integration.controller;

import com.razorpay.razorpay_integration.models.PaymentDetails;
import com.razorpay.razorpay_integration.models.PaymentResponse;
import com.razorpay.razorpay_integration.models.PaymentStatus;
import com.razorpay.razorpay_integration.models.Status;
import com.razorpay.razorpay_integration.service.PaymentService;
import com.razorpay.razorpay_integration.service.RazorPayGateway;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
public class PaymentController {

    private final PaymentService paymentService;
    private final RazorPayGateway razorPayGateway;

    @PostMapping("/payment/createLink")
    public String createPaymentLink(@RequestParam String orderId){
        return paymentService.createLink(orderId);
    }

    @GetMapping("/payment/getPaymentStatus")
    public PaymentStatus getPaymentStatus(@RequestParam("paymentId") String paymentId, @RequestParam("orderId") String orderId){
        return paymentService.getPaymentStatus(paymentId, orderId);
    }

    @GetMapping("/payment/getPaymentDetails")
    public PaymentDetails getPaymentDetails(@RequestParam("paymentId")String paymentId)
    {
        return razorPayGateway.getPaymentInfo(paymentId);
    }

    @GetMapping("/payment/getStatus")
    public Status getStatus(@RequestParam("paymentId")String paymentId){
        return razorPayGateway.getStatuss(paymentId);
    }
}