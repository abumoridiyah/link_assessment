package com.link.Assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    @NotBlank(message = "Name cannot be blank")
    private String companyName;
    @Email(message = "Please provide a valid email address")
    private String companyEmail;
    @Pattern(regexp = "\\d{11}", message = "Please provide a valid 11-digit mobile number")
    private String companyMobile;
    private String merchantId;
}
