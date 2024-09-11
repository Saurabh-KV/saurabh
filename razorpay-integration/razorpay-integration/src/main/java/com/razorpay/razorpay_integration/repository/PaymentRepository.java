package com.razorpay.razorpay_integration.repository;

import com.razorpay.razorpay_integration.models.PaymentDetails;
import com.razorpay.razorpay_integration.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetails,Long> {

    Optional<PaymentDetails> findByOrderId(String orderId);

    Optional<PaymentDetails> findByPaymentId(String paymentId);
}
