package co.dhan.dto;

public record EDISStatus (
    String clientId,
    String isin,
    String totalQty,
    String aprvdQty,
    String status,
    String remarks
) {}
