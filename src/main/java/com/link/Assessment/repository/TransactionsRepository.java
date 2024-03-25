package com.link.Assessment.repository;

import com.link.Assessment.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByInvoiceNo(String invoiceNo);
    @Query("SELECT SUM(t.totalAmount) FROM Transactions t WHERE t.invoiceNo = :invoiceNo")
    BigDecimal getTotalAmount(@Param("invoiceNo") String invoiceNo);

}
