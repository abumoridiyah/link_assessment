package com.link.Assessment.serviceImpl;

import com.link.Assessment.components.ReceiptGenerator;
import com.link.Assessment.dto.*;
import com.link.Assessment.model.Merchants;
import com.link.Assessment.model.Payment;
import com.link.Assessment.model.Transactions;
import com.link.Assessment.repository.MerchantsRepository;
import com.link.Assessment.repository.PaymentRepository;
import com.link.Assessment.repository.TransactionsRepository;
import com.link.Assessment.service.PaymentService;
import com.link.Assessment.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class PaymentServiceImpl implements PaymentService {
    @Value("${INVOICE_DETAILS_LINK}")
    private String invoice_details_link;
    @Value("${PAYMENT_LINK}")
    private String payment_link;
    @Autowired
    MerchantsRepository merchantsRepository;
    @Autowired
    TransactionsRepository transactionsRepository;
    @Autowired
    PaymentRepository paymentRepository;
    private static final char KEY = 'K';
    @Autowired
    private JavaMailSender emailSender;

    @PostConstruct
    private void addRecords() {
        MerchantRequest merchantRequest = createMerchantRequest();
        TransactionsRequest transactionsRequest = createTransactionsRequest(merchantRequest);
        saveMerchant(merchantRequest);
        saveTransactions(transactionsRequest);
    }

    private MerchantRequest createMerchantRequest() {
        return MerchantRequest.builder()
            .companyEmail("yusuf@swipe.ng")
            .companyMobile("08060003318")
            .companyName("Softminds Tech Limited")
            .merchantId("11209467890")
            .build();
    }

    private TransactionsRequest createTransactionsRequest(MerchantRequest merchantRequest) {
        List<SalesRequest> salesRequestList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        // Format the date without hyphens
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        salesRequestList.add(createSalesRequest("Puma Shoe", 2, 50000.00));
        salesRequestList.add(createSalesRequest("Hand Bag", 4, 100000.00));
        return TransactionsRequest.builder()
            .invoiceNo("INV" + formattedDate + "002")
            .customerName("Mr Solomon David")
            .merchantId(merchantRequest.getMerchantId())
            .salesRequestList(salesRequestList)
            .build();
    }

    private SalesRequest createSalesRequest(String productName, int quantity, double unitPrice) {
        return SalesRequest.builder()
            .productName(productName)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .totalAmount(quantity * unitPrice)
            .build();
    }

    private void saveMerchant(MerchantRequest request) {
        Merchants merchants = new Merchants();
        merchants.setMerchantId(request.getMerchantId());
        LocalDateTime now = LocalDateTime.now();
        merchants.setDateModified(now);
        merchants.setDateCreated(now);
        merchants.setCompanyName(request.getCompanyName());
        merchants.setCompanyMobile(request.getCompanyMobile());
        merchants.setCompanyEmail(request.getCompanyEmail());
        merchantsRepository.save(merchants);
    }

    private void saveTransactions(TransactionsRequest request) {
        List<Transactions> transactionsList = request.getSalesRequestList().stream()
            .map(rs -> {
                Transactions transactions = new Transactions();
                transactions.setQuantity(rs.getQuantity());
                transactions.setProductName(rs.getProductName());
                transactions.setTotalAmount(rs.getTotalAmount());
                transactions.setInvoiceNo(request.getInvoiceNo());
                transactions.setMerchantId(request.getMerchantId());
                transactions.setCustomerName(request.getCustomerName());
                transactions.setDateCreated(LocalDateTime.now());
                transactions.setDateModified(LocalDateTime.now());
                transactions.setUnitPrice(rs.getUnitPrice());
                return transactions;
            })
            .collect(Collectors.toList());
        transactionsRepository.saveAll(transactionsList);
    }

    @Override
    public ResponseEntity generateInvoiceDetailsLink(PaymentDto payload) throws AddressException {
        Optional<Merchants> optionalMerchants = merchantsRepository.findByMerchantId(payload.getMerchantId());
        if (optionalMerchants.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(MessageUtils.FAILED, MessageUtils.NOT_AVAILABLE, MessageUtils.NOT_FOUND));
        }
        List<Transactions> optionalTransactions = transactionsRepository.findByInvoiceNo(payload.getInvoiceNo());
        if (optionalTransactions.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(MessageUtils.FAILED, MessageUtils.NOT_AVAILABLE, MessageUtils.NOT_FOUND));
        }
        String tokens = encrypt(payload.getInvoiceNo());
        String activationLink = invoice_details_link + "/" + tokens;
        sendMessage(payload.getCustomerEmail(), "Invoice Details Link", optionalTransactions.get(0).getCustomerName(), activationLink);
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));
    }

    public static String encrypt(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] ^ KEY);
        }
        return new String(chars);
    }

    public static String decrypt(String input) {
        return encrypt(input);
    }

    public void sendMessage(String to, String subject, String name, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Hello " + name + ", click this link " + text + " to view your invoice details. Thanks");
        emailSender.send(message);
    }

    public StreamingResponseBody invoiceDetails(HttpServletResponse response, String tokens) throws IOException {
        String invoiceNo = decrypt(tokens);
        String paymentLink = payment_link + "/" + tokens;
        List<Transactions> details = transactionsRepository.findByInvoiceNo(invoiceNo);
        Optional<Merchants> merchants = merchantsRepository.findByMerchantId(details.get(0).getMerchantId());
        ByteArrayInputStream bis = ReceiptGenerator.generatePDFReport(details, merchants.get(), paymentLink);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
            "inline; filename=invoicedetails.pdf");
        response.setHeader("Cache-Control",
            "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return outputStream -> {
            int nRead;
            byte[] data = new byte[bis.available()];
            while ((nRead = bis.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            outputStream.flush();
            outputStream.close();
        };
    }

    @Override
    public ResponseEntity<?> makePayment(MakePaymentDto payload, String token) {
        // Decrypt the token to get the invoice number
        String invoiceNo = decrypt(token);

// Retrieve the total amount from the database
        BigDecimal totalAmount = transactionsRepository.getTotalAmount(invoiceNo);

// Check if the provided amount matches the total amount from the database
        if (totalAmount == null || !totalAmount.equals(payload.getAmount())) {
            // Amounts do not match, return an error response
            return ResponseEntity.ok(new ApiResponse<>(MessageUtils.FAILED, MessageUtils.AMOUNT_NOT_MATCH));
        }

// Amounts match, proceed with saving the payment record
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setInvoiceNo(invoiceNo);
        paymentRepository.save(payment);

// Return a success response
        return ResponseEntity.ok(new ApiResponse<>(MessageUtils.SUCCESS, MessageUtils.OKAY, MessageUtils.DONE));

    }
}
