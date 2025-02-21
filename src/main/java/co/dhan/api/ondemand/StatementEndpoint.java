package co.dhan.api.ondemand;

import co.dhan.api.DhanConnection;
import co.dhan.dto.Ledger;
import co.dhan.http.DhanAPIException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatementEndpoint {

    interface APIParam {
        String FromDate = "from-date";
        String ToDate = "to-date";
    }

    interface APIEndpoint {
        String LedgerReport = "/ledger?from-date=%s&to-date=%s";
    }

    private final DhanConnection dhanConnection;

    public StatementEndpoint(DhanConnection dhanConnection) {
        this.dhanConnection = dhanConnection;
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @return
     * @throws DhanAPIException
     */
    public Ledger getLedgerReport(LocalDate fromDate, LocalDate toDate) throws DhanAPIException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endpoint = String.format(APIEndpoint.LedgerReport,
                fromDate.format(pattern),
                toDate.format(pattern));
        return dhanConnection.getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Ledger.class);
    }
}
