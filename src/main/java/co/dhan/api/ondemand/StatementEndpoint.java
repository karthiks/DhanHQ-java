package co.dhan.api.ondemand;

import co.dhan.api.DhanContext;
import co.dhan.http.DhanAPIException;
import co.dhan.dto.Ledger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatementEndpoint {

    interface APIParam {
        String FromDate = "from-date";
        String ToDate = "to-date";
    }

    interface APIEndopint {
        String LedgerReport = "/ledger?from-date=%s&to-date=%s";
    }

    private final DhanContext dhanContext;

    public StatementEndpoint(DhanContext dhanContext) {
        this.dhanContext = dhanContext;
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
        String endpoint = String.format(APIEndopint.LedgerReport,
                fromDate.format(pattern),
                toDate.format(pattern));
        return dhanContext.getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(Ledger.class);
    }
}
