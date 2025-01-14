package co.dhan.dto;

import lombok.Data;

@Data
public class EDISStatus {
    private String clientId;
    private String isin;
    private String totalQty;
    private String aprvdQty;
    private String status;
    private String remarks;
}
