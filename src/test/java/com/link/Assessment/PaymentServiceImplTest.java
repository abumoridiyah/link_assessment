package com.link.Assessment;


import com.link.Assessment.dto.ApiResponse;
import com.link.Assessment.dto.PaymentDto;
import com.link.Assessment.model.Merchants;
import com.link.Assessment.model.Transactions;
import com.link.Assessment.repository.MerchantsRepository;
import com.link.Assessment.repository.TransactionsRepository;
import com.link.Assessment.serviceImpl.PaymentServiceImpl;
import com.link.Assessment.utils.MessageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.AddressException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {

    @Mock
    private MerchantsRepository merchantsRepository;

    @Mock
    private TransactionsRepository transactionsRepository;
    @Mock
    JavaMailSender emailSender;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void testGenerateInvoiceDetailsLink_Success() throws AddressException {
        // Prepare test data
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCustomerEmail("us@ourmail.com");
        paymentDto.setInvoiceNo("09876567");
        paymentDto.setMerchantId("oiuytrertyu");

        // Mock the behavior of merchantsRepository.findByMerchantId to return a valid merchant
        when(merchantsRepository.findByMerchantId(any())).thenReturn(getOptionalMerchants());

        // Mock the behavior of transactionsRepository.findByInvoiceNo to return a non-empty list
        when(transactionsRepository.findByInvoiceNo(any())).thenReturn(Collections.singletonList(new Transactions()));

        // Call the method under test
        ResponseEntity<?> response = paymentService.generateInvoiceDetailsLink(paymentDto);

        // Verify that the response indicates success
        ResponseEntity<?> expectedResponse = ResponseEntity.ok(new ApiResponse<>(
            MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));
        assertEquals(expectedResponse, response);
    }
    @Test
    public void testGenerateInvoiceDetailsLink_RecordNotFound() throws AddressException {
        // Prepare test data
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCustomerEmail("us@ourmail.com");
        paymentDto.setInvoiceNo("09876567");
        paymentDto.setMerchantId("oiuytrertyu");

        // Mock the behavior of merchantsRepository.findByMerchantId to return a valid merchant
        when(merchantsRepository.findByMerchantId(any())).thenReturn(getOptionalMerchants());

        // Mock the behavior of transactionsRepository.findByInvoiceNo to return an empty list
        when(transactionsRepository.findByInvoiceNo(any())).thenReturn(Collections.emptyList());

        // Call the method under test
        ResponseEntity<?> response = paymentService.generateInvoiceDetailsLink(paymentDto);

        // Verify that the response indicates record not found
        ResponseEntity<?> expectedResponse = ResponseEntity.ok(new ApiResponse<>(
            MessageUtils.FAILED, MessageUtils.NOT_AVAILABLE, MessageUtils.NOT_FOUND));
        assertEquals(expectedResponse, response);
    }

    // Helper method to create a mocked Optional<Merchants> instance
    private Optional<Merchants> getOptionalMerchants() {
        Merchants merchants = new Merchants();
        merchants.setMerchantId("09876556");
        merchants.setCompanyMobile("0908765678");
        merchants.setCompanyName("Mr Niger D");
        return Optional.of(merchants);
    }

    // Helper method to create a mocked list of Transactions
    private List<Transactions> getTransactions() {
        // Create and return a list of Transactions
        Transactions transactions = new Transactions();
        transactions.setQuantity(3);
        transactions.setUnitPrice(5000.00);
        transactions.setCustomerName("Adediran");
        transactions.setInvoiceNo("098765456");
        return List.of(transactions);
    }

    // Helper method to create a PaymentDto instance
    private PaymentDto createPaymentDto() {
        // Create and return a PaymentDto instance
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCustomerEmail("adediran@gmail.com");
        paymentDto.setMerchantId("987654567");
        paymentDto.setInvoiceNo("09876556");
        return paymentDto;
    }

    // Helper method to create a success response
    private ResponseEntity<?> getSuccessResponse() {
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));
    }
}
