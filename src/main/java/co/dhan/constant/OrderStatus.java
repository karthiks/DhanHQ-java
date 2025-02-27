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
    EXPIRED("Validity of order is expired");

    private String description;
}
