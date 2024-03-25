package com.link.Assessment.service;

import com.link.Assessment.dto.MerchantRequest;
import com.link.Assessment.dto.UpdateMerchantRequest;
import com.link.Assessment.enums.SortDirections;
import org.springframework.http.ResponseEntity;

public interface MerchantService {
    ResponseEntity<?> createMerchant(MerchantRequest request);
    ResponseEntity<?> updateMerchant(UpdateMerchantRequest request);
    ResponseEntity<?> getMerchants(int size, int page, SortDirections sortDirections);
    ResponseEntity<?> getMerchantById(String merchantId);
    ResponseEntity<?> deleteMerchant(String merchantId);
}
