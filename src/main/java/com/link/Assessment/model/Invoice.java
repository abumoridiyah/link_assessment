package com.link.Assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "invoice_no")
    private String invoiceNo;
    @Column(name ="company_name")
    private String companyName;
    @Column(name ="total_amount")
    private Double totalAmount;
    @Column(name="date_created")
    private LocalDateTime dateCreated;
    @Column(name="date_modified")
    private LocalDateTime dateModified;
}
