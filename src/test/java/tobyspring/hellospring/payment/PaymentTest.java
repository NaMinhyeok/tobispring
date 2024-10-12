package tobyspring.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

class PaymentTest {

    Clock clock;
    ExRateProvider exRateProvider;

    @BeforeEach
    void setUp() {
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        this.exRateProvider = new ExRateProviderStub(BigDecimal.valueOf(1_000));
    }

    @DisplayName("payment 생성 테스트")
    @Test
    void createPrepared() {
        //given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        //when
        Payment payment = Payment.createPrepared(1L,"USD", BigDecimal.TEN, LocalDateTime.now(clock), exRateProvider);

        //then
        Assertions.assertThat(payment.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(10_000));
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(LocalDateTime.now(clock).plusMinutes(30));
    }

    @DisplayName("validateion 테스트")
    @Test
    void test() {
        //given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(1L,"USD", BigDecimal.ONE, LocalDateTime.now(clock), exRateProvider);
        //when
        //then
        Assertions.assertThat(payment.isValid(clock)).isTrue();
        Assertions.assertThat(payment.isValid(Clock.offset(clock, Duration.of(30, ChronoUnit.MINUTES)))).isFalse();
    }

}