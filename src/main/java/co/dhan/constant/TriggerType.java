package co.dhan.constant;

/** Represents the type of trigger condition for super orders. */
public enum TriggerType {
  /** Trigger based on price conditions */
  PRICE_TRIGGER,

  /** Trigger based on quantity/filled conditions */
  QUANTITY_TRIGGER,

  /** Trigger based on time conditions */
  TIME_TRIGGER
}
