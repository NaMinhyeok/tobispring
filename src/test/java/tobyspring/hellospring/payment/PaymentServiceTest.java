package tobyspring.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.hellospring.exrate.WebApiExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

class PaymentServiceTest {

    Clock clock;

    @BeforeEach
    void setUp() {
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }

    @Test
    void prepare() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        PaymentService paymentService = new PaymentService(new WebApiExRateProvider(),clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.ONE);

        // 환율 정보 가져오는지 확인
        assertThat(payment.getExRate()).isNotNull();

        // 원화 환산 금액 계산
        assertThat(payment.getConvertedAmount())
            .isEqualTo(payment.getExRate().multiply(payment.getForeignCurrencyAmount()));

        // 원화 환산금액의 유효시간 계산
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));
    }

    @DisplayName("stub을 이용한 테스트")
    @Test
    void test() {

        testAmount(BigDecimal.valueOf(500),BigDecimal.valueOf(5_000),this.clock);
        testAmount(BigDecimal.valueOf(1_000),BigDecimal.valueOf(10_000),this.clock);
        testAmount(BigDecimal.valueOf(3_000),BigDecimal.valueOf(30_000),this.clock);

        // 원화 환산금액의 유효시간 계산
//        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
//        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMinutes(30));
    }

    @DisplayName("유효시간 계산")
    @Test
    void validUntil() {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(BigDecimal.valueOf(1_000)), clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);

        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }

    private void testAmount(BigDecimal exRate, BigDecimal convertedAmount, Clock clock)  {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(exRate),clock);

        Payment payment = paymentService.prepare(10L, "USD", BigDecimal.TEN);

        assertThat(payment.getExRate()).isEqualByComparingTo(exRate);
        assertThat(payment.getConvertedAmount()).isEqualByComparingTo(convertedAmount);
    }
}