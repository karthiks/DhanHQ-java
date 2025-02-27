package co.dhan.api.ondemand;

import co.dhan.api.ITest_DhanTestRoot;
import co.dhan.dto.Order;
import co.dhan.dto.OrderResponse;
import co.dhan.http.DhanAPIException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ITest_OrderEndpoint extends ITest_DhanTestRoot {

    @Test
    void getOrdersSuccessfully() throws DhanAPIException, IOException {
        List<Order> orderList = dhanCore.getOrderEndpoint().getCurrentOrders();
        assertThat(orderList).isNotEmpty();
    }

    @Test
    void getOrderByIdSuccessfully() throws DhanAPIException, IOException {
        Order order = dhanCore.getOrderEndpoint().getOrderByID("orderid");
        assertThat(order).isNotNull();
    }

    @Test
    void cancelOrderSuccessfully() throws DhanAPIException, IOException {
        OrderResponse orderStatus = dhanCore.getOrderEndpoint().cancelOrder("orderid");
        assertThat(orderStatus).isNotNull();
    }
}