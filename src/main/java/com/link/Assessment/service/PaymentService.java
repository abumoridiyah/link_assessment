package com.link.Assessment.service;

import com.link.Assessment.dto.MakePaymentDto;
import com.link.Assessment.dto.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PaymentService {
    ResponseEntity<?> generateInvoiceDetailsLink(PaymentDto payload) throws AddressException;
    StreamingResponseBody invoiceDetails(HttpServletResponse response, String invoiceNo) throws IOException;
    ResponseEntity<?> makePayment(MakePaymentDto payload,String token);
}
