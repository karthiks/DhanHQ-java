package co.dhan.api;

import co.dhan.dto.OrderStatusDTO;
import co.dhan.http.DhanAPIException;
import co.dhan.dto.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class E2EOrdersTest extends E2EDhanTestRoot {

    @Test
    void getOrdersSuccessfully() throws DhanAPIException, IOException {
        List<Order> orderList = dhanCore.getOrderEndpoint().getCurrentOrders();
//        System.out.println("--Order List--");
//        orderList.forEach(System.out::println);
        assertThat(orderList).isNotEmpty();
    }

    @Test
    void getOrderByIdSuccessfully() throws DhanAPIException, IOException {
        Order order = dhanCore.getOrderEndpoint().getOrderByID("orderid");
        System.out.println("--Order--");
        System.out.println(order);
        assertThat(order).isNotNull();
    }

    @Test
    void cancelOrderSuccessfully() throws DhanAPIException, IOException {
        OrderStatusDTO orderStatus = dhanCore.getOrderEndpoint().cancelOrder("orderid");
        System.out.println("--Order--");
        System.out.println(orderStatus);
        assertThat(orderStatus).isNotNull();
    }
}