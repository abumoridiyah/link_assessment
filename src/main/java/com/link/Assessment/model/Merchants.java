package com.link.Assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchants implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name ="company_email")
    private String companyEmail;
    @Column(name ="company_mobile")
    private String companyMobile;
    @Column(name ="merchant_id")
    private String merchantId;
    @Column(name="date_created")
    private LocalDateTime dateCreated;
    @Column(name="date_modified")
    private LocalDateTime dateModified;
}
