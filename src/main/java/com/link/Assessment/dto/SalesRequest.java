package com.link.Assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesRequest {
    private String productName;
    private int quantity;
    private Double unitPrice;
    private Double totalAmount;
}
