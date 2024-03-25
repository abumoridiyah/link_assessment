package com.link.Assessment.repository;

import com.link.Assessment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByInvoiceNo(String invoiceNo);
}
