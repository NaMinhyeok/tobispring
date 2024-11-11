package tobyspring.hellospring.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.hellospring.OrderConfig;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderConfig.class)
class OrderServiceSpringTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        var order = orderService.createOrder("0100", BigDecimal.ONE);
        assertThat(order.getId()).isPositive();
        assertThat(order).extracting(Order::getId, Order::getNo, Order::getTotal)
            .containsExactly(order.getId(), "0100", BigDecimal.ONE);
    }

}
