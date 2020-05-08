package io.github.kkw.payments.controllers;

import io.github.kkw.payments.model.Payment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentsController {
    private static final String PASSWORD = "supersecretTO2";
    private final Map<String, String> methods;

    public PaymentsController() {
        methods = new HashMap<>();
        methods.put("seat", "finishPaymentSeat");
        methods.put("advanceHall", "finishAdvancePaymentSeat");
        methods.put("hall", "finishPaymentHall");
    }

    @GetMapping("/pay/{reservationId}")
    public String pay(@PathVariable("reservationId") int reservationId,
                      @RequestParam("clientId") int clientId,
                      @RequestParam("amount") double amount,
                      @RequestParam("type") String type,
                      Model model) {
        model.addAttribute("payment", new Payment(clientId, amount, methods.get(type), reservationId));
        return "payment-form";
    }

    @PostMapping("/pay")
    public String finishPayment(@ModelAttribute("payment") @Valid Payment payment,
                                BindingResult bindingResult,
                                Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            final URL url = new URL("http://localhost:8888/" + payment.getMethod() + "/" + payment.getReservationId() +
                    "?clientId=" + payment.getClientId() + "&password=" + PASSWORD);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            final int status = conn.getResponseCode();
            if (status != 200) {
                model.addAttribute("fail", "Payment failed");
                return "payment-form";
            }
            model.addAttribute("success", "Payment successful");
            return "success";
        }
        return "payment-form";
    }
}
