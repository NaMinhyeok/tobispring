package tobyspring.hellospring.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.hellospring.OrderConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderConfig.class)
class OrderServiceSpringTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DataSource dataSource;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        var order = orderService.createOrder("0100", BigDecimal.ONE);
        assertThat(order.getId()).isPositive();
        assertThat(order).extracting(Order::getId, Order::getNo, Order::getTotal)
            .containsExactly(order.getId(), "0100", BigDecimal.ONE);
    }

    @DisplayName("다수의 주문을 생성한다.")
    @Test
    void createOrders() {
        //given
        List<OrderRequest> orderRequests = List.of(
            new OrderRequest("0200", BigDecimal.ONE),
            new OrderRequest("0201", BigDecimal.TWO)
        );
        //when
        var orders = orderService.createOrders(orderRequests);
        //then
        assertThat(orders).hasSize(2)
            .extracting(Order::getId, Order::getNo, Order::getTotal)
            .containsExactlyInAnyOrder(
                tuple(orders.getFirst().getId(), "0200", BigDecimal.ONE),
                tuple(orders.get(1).getId(), "0201", BigDecimal.TWO)
            );
    }

    @DisplayName("다수의 중복번호 주문을 생성한다.")
    @Test
    void createDuplicatedOrders() {
        //given
        List<OrderRequest> orderRequests = List.of(
            new OrderRequest("0300", BigDecimal.ONE),
            new OrderRequest("0300", BigDecimal.TWO)
        );
        //when
        //then
        assertThatThrownBy(() -> orderService.createOrders(orderRequests))
            .isInstanceOf(DataIntegrityViolationException.class);

        JdbcClient client = JdbcClient.create(dataSource);
        Long count = client.sql("select count(*) from orders where no = '0300'")
            .query(Long.class)
            .single();
        assertThat(count).isZero();
    }

}
