package co.dhan.dto;

import lombok.Data;

@Data
public class Ledger {
    private String dhanClientId;
    private String narration;
    private String voucherdate;
    private String exchange;
    private String voucherdesc;
    private String vouchernumber;
    private String debit;
    private String credit;
    private String runbal;
}
