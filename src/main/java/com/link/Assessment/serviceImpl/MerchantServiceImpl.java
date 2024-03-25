package com.link.Assessment.serviceImpl;

import com.link.Assessment.dto.ApiResponse;
import com.link.Assessment.dto.MerchantRequest;
import com.link.Assessment.dto.MerchantResponse;
import com.link.Assessment.dto.UpdateMerchantRequest;
import com.link.Assessment.enums.SortDirections;
import com.link.Assessment.enums.SortFields;
import com.link.Assessment.exceptions.ResourceNotFoundException;
import com.link.Assessment.model.Merchants;
import com.link.Assessment.repository.MerchantsRepository;
import com.link.Assessment.service.MerchantService;
import com.link.Assessment.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    MerchantsRepository merchantsRepository;

    @Override
    public ResponseEntity<?> createMerchant(MerchantRequest request) {
        // check if the company name has been registered to avoid repetitions
        Optional<Merchants> existingMerchant = merchantsRepository.findByCompanyEmailOrCompanyMobile(request.getCompanyEmail(), request.getCompanyMobile());

        if (existingMerchant.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(MessageUtils.FAILED, MessageUtils.NOT_AVAILABLE, MessageUtils.RECORD_EXISTS));
        }

        Merchants newMerchant = new Merchants();
        String merchantId = String.valueOf(System.currentTimeMillis());
        newMerchant.setMerchantId(merchantId);
        newMerchant.setDateCreated(LocalDateTime.now());
        newMerchant.setCompanyName(request.getCompanyName());
        newMerchant.setCompanyEmail(request.getCompanyEmail());
        newMerchant.setDateModified(LocalDateTime.now());
        newMerchant.setCompanyMobile(request.getCompanyMobile());

        merchantsRepository.save(newMerchant);

        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));

    }

    @Override
    public ResponseEntity<?> updateMerchant(UpdateMerchantRequest request) {
        Optional<Merchants> optionalMerchant = merchantsRepository.findByMerchantId(request.getMerchantId());
        Merchants updatedMerchant = optionalMerchant.orElseThrow(() -> new ResourceNotFoundException("Merchant with ID: " + request.getMerchantId() + " not found"));

        // Update merchant details
        updatedMerchant.setCompanyMobile(request.getCompanyMobile());
        updatedMerchant.setCompanyEmail(request.getCompanyEmail());
        updatedMerchant.setCompanyName(request.getCompanyName());
        updatedMerchant.setDateModified(LocalDateTime.now());

        // Save the updated merchant
        merchantsRepository.save(updatedMerchant);

        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));
    }

    @Override
    public ResponseEntity<?> getMerchants(int size, int page, SortDirections sortDirections) {
        Sort orders = sortDirections.label.equalsIgnoreCase(SortDirections.DESCENDING.label) ? Sort.by(SortFields.ID.label).descending() : Sort.by(SortFields.ID.label).ascending();
        Page<Merchants> merchants = merchantsRepository.findAll(PageRequest.of(page, size, orders));
        log.info("###############{}",merchants.getContent());
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, mapToMerchants(merchants.getContent())));
    }

    @Override
    public ResponseEntity<?> getMerchantById(String merchantId) {
        Optional<Merchants> optionalMerchant = merchantsRepository.findByMerchantId(merchantId);
        Merchants merchants = optionalMerchant.orElseThrow(() -> new ResourceNotFoundException("Merchant with ID: " + merchantId + " not found"));
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, mapToMerchantResponse(merchants)));
    }


    @Override
    public ResponseEntity<?> deleteMerchant(String merchantId) {
        Optional<Merchants> optionalMerchant = merchantsRepository.findByMerchantId(merchantId);
        Merchants merchants = optionalMerchant.orElseThrow(() -> new ResourceNotFoundException("Merchant with ID: " + merchantId + " not found"));
        merchantsRepository.delete(merchants);
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));
    }

    private List<MerchantResponse> mapToMerchants(List<Merchants> response) {
        return response.stream()
            .map(this::mapToMerchantResponse)
            .collect(Collectors.toList());
    }

    private MerchantResponse mapToMerchantResponse(Merchants merchant) {
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
