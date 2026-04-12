package co.dhan.dto;

import co.dhan.constant.SuperOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for super order operations (create, modify, cancel). Contains the super order ID
 * and its current status.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperOrderResponse {
  /** Unique identifier for the super order */
  private String orderId;

  /** Current status of the super order */
  private SuperOrderStatus orderStatus;
}
