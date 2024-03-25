package com.link.Assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantResponse {
    private String companyName;
    private String companyEmail;
    private String companyMobile;
    private String merchantId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
}
