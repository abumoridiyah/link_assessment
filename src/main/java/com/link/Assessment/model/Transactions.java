package com.link.Assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name ="product_name")
    private String productName;
    @Column(name ="quantity")
    private int quantity;
    @Column(name ="invoice_no")
    private String invoiceNo;
    @Column(name ="unit_price")
    private Double unitPrice;
    @Column(name ="total_amount")
    private Double totalAmount;
    @Column(name="date_created")
    private LocalDateTime dateCreated;
    @Column(name="date_modified")
    private LocalDateTime dateModified;
    @Column(name="merchant_id")
    private String merchantId;
}
