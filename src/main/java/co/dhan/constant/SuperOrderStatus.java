package co.dhan.constant;

/** Represents the status of a super order in the Dhan trading system. */
public enum SuperOrderStatus {
  /** Super order has been created but not yet processed */
  PENDING,

  /** Super order is partially filled (some legs executed) */
  PARTIALLY_FILLED,

  /** Super order is completely filled (all legs executed) */
  FILLED,

  /** Super order has been cancelled */
  CANCELLED,

  /** Super order was rejected by the exchange */
  REJECTED,

  /** Super order has expired */
  EXPIRED
}
