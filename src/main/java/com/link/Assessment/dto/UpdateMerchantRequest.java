package com.link.Assessment.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateMerchantRequest {
    @NotBlank(message = "Merchant ID cannot be blank")
    private String merchantId;
    @NotBlank(message = "Name cannot be blank")
    private String companyName;
    @Email(message = "Please provide a valid email address")
    private String companyEmail;
    @Pattern(regexp = "\\d{11}", message = "Please provide a valid 11-digit mobile number")
    private String companyMobile;
}
