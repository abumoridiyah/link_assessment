package com.link.Assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsRequest {
    private String merchantId;
    private String invoiceNo;
    private String customerName;
    List<SalesRequest> salesRequestList;
}
