package com.link.Assessment.controller;

import com.link.Assessment.dto.MerchantRequest;
import com.link.Assessment.dto.UpdateMerchantRequest;
import com.link.Assessment.enums.SortDirections;
import com.link.Assessment.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    MerchantService merchantService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MerchantRequest payload){
        return merchantService.createMerchant(payload);
    }
    @PutMapping("/update")
    ResponseEntity<?> updateMerchant(@RequestBody UpdateMerchantRequest request){
       return merchantService.updateMerchant(request);
    }
    @GetMapping("/get-all")
    ResponseEntity<?> getMerchants(@RequestParam("size") int size,@RequestParam("page") int page,
                                   @RequestParam("sortDirections")SortDirections sortDirections){
        return merchantService.getMerchants(size,page,sortDirections);
    }
    @GetMapping("/get/{merchantId}")
    ResponseEntity<?> getMerchantById(@PathVariable("merchantId") String merchantId){
        return merchantService.getMerchantById(merchantId);
    }
    @DeleteMapping("/{merchantId}")
    ResponseEntity<?> deleteMerchant(@PathVariable("merchantId") String merchantId){
        return merchantService.deleteMerchant(merchantId);
    }

}
