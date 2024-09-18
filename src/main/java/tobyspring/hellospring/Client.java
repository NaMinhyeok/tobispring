package tobyspring.hellospring;

import java.math.BigDecimal;

public class Client {
    public static void main(String[] args) throws Exception {
        PaymentService paymentService = new PaymentService(new WebApiExRateProvider());
        Payment payment = paymentService.prepare(1L,"USD", BigDecimal.valueOf(13.8));
        System.out.println(payment);
    }
}
