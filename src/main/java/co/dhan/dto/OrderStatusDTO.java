package co.dhan.dto;

import co.dhan.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDTO {
    String orderId;
    OrderStatus orderStatus;
}
