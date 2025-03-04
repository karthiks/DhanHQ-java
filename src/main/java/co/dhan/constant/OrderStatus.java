package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    TRANSIT("Did not reach the exchange server "),
    PENDING("Reached at exchange end, awaiting execution"),
    REJECTED("Rejected at exchange/broker’s end"),
    CANCELLED("Cancelled by user"),
    PART_TRADED("Partially Executed"),
    TRADED("Executed"),
    CONFIRM("Confirm"), // Why not TRADED?? https://dhanhq.co/docs/v2/forever/#all-forever-order-detail
    EXPIRED("Validity of order is expired");

    private String description;
}
