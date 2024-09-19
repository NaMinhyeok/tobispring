package tobyspring.hellospring;

public class ObjectFactory {
    public PaymentService paymentService() {
        return new PaymentService(new SimpleExRateProvider());
    }

    public ExRateProvider exRateProvider() {
        return new SimpleExRateProvider();
    }
}
