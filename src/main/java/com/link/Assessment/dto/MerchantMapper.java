package com.link.Assessment.dto;

import com.link.Assessment.model.Merchants;

import java.util.List;
import java.util.stream.Collectors;

public class MerchantMapper {

    public static List<MerchantResponse> mapToMerchants(List<Merchants> response) {
        return response.stream()
                .map(MerchantMapper::mapToMerchantResponse)
                .collect(Collectors.toList());
    }

    private static MerchantResponse mapToMerchantResponse(Merchants merchant) {
        return MerchantResponse.builder()
                .companyEmail(merchant.getCompanyEmail())
                .companyMobile(merchant.getCompanyMobile())
                .companyName(merchant.getCompanyName())
                .dateCreated(merchant.getDateCreated())
                .dateModified(merchant.getDateModified())
                .merchantId(merchant.getMerchantId())
                .build();
    }
}
