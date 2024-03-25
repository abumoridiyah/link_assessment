package com.link.Assessment.controller;

import com.link.Assessment.dto.ApiResponse;
import com.link.Assessment.dto.MakePaymentDto;
import com.link.Assessment.dto.PaymentDto;
import com.link.Assessment.service.PaymentService;
import com.link.Assessment.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping("generate/invoice-details")
    public ResponseEntity<?> generateInvoiceDetailsLink(@RequestBody PaymentDto payload) throws AddressException {
        return paymentService.generateInvoiceDetailsLink(payload);
    }

    @GetMapping("/invoiceDetails/{tokens}")
    StreamingResponseBody invoiceDetails(
        HttpServletResponse response,@PathVariable(value = "tokens", required = true) String tokens) throws IOException {
        return paymentService.invoiceDetails(response, tokens);
    }
    @PostMapping("/{tokens}")
    public ResponseEntity<?> makePayment(@PathVariable("tokens") String tokens,@RequestBody MakePaymentDto makePaymentDto){
        return paymentService.makePayment(makePaymentDto,tokens);
    }
}
