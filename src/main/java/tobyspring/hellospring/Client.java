package tobyspring.hellospring;

import java.math.BigDecimal;

public class Client {
    public static void main(String[] args) throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        PaymentService paymentService = objectFactory.paymentService();

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.valueOf(13.8));
        System.out.println(payment);
    }
}
