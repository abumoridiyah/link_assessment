package com.link.Assessment.repository;

import com.link.Assessment.model.Merchants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MerchantsRepository extends JpaRepository<Merchants,Long>{
    Optional<Merchants> findByCompanyEmail(String email);
    Optional<Merchants> findByCompanyEmailOrCompanyMobile(String email, String mobile);
    @Query("select st from Merchants st where st.merchantId=:merchantId")
    Optional<Merchants> findByMerchantId(String merchantId);
    Page<Merchants> findAll(Pageable pageable);
}
