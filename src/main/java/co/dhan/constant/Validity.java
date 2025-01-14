package co.dhan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Validity {
    DAY("Valid till end of day"),
    IOC("Immediate or Cancel");

    private String description;
}
