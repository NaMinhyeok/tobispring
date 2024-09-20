package tobyspring.hellospring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tobyspring.hellospring.payment.Payment;
import tobyspring.hellospring.payment.PaymentService;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(ObjectFactory.class);
        PaymentService paymentService = beanFactory.getBean(PaymentService.class);

        Payment payment1 = paymentService.prepare(1L, "USD", BigDecimal.valueOf(13.8));
        System.out.println("Payment1 : " + payment1);

        System.out.println("-------------------------------------------------------");
        TimeUnit.SECONDS.sleep(3);

        Payment payment2 = paymentService.prepare(1L, "USD", BigDecimal.valueOf(13.8));
        System.out.println("Payment2 : " + payment2);
        System.out.println("-------------------------------------------------------");

        TimeUnit.SECONDS.sleep(5);

        Payment payment3 = paymentService.prepare(1L, "USD", BigDecimal.valueOf(13.8));
        System.out.println("Payment3 : " + payment3);
    }
}
