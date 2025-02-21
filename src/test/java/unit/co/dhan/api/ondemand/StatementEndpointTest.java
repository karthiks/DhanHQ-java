package co.dhan.api.ondemand;

import co.dhan.UnitTestRoot;
import co.dhan.api.DhanConnection;
import co.dhan.dto.Ledger;
import co.dhan.http.DhanHTTP;
import co.dhan.http.DhanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatementEndpointTest extends UnitTestRoot {

    @Mock
    DhanConnection mockDhanConnection;

    @Mock
    DhanHTTP mockDhanHTTP;

    @Mock
    DhanResponse mockDhanResponse;

    @Spy
    @InjectMocks
    StatementEndpoint statementEndpoint;

    @Test
    void getLedgerReport_ReturnsResult() {
        LocalDate fromDate = LocalDate.of(2025,01,01);
        LocalDate toDate = LocalDate.of(2025,01,31);;
        Ledger expectedLedger = new Ledger();
        when(mockDhanConnection.getDhanHTTP()).thenReturn(mockDhanHTTP);
        when(mockDhanHTTP.doHttpGetRequest(anyString())).thenReturn(mockDhanResponse);
        when(mockDhanResponse.convertToType(Ledger.class)).thenReturn(expectedLedger);

        assertThat(statementEndpoint.getLedgerReport(fromDate, toDate)).isEqualTo(expectedLedger);
        verify(mockDhanHTTP).doHttpGetRequest(eq("/ledger?from-date=2025-01-01&to-date=2025-01-31"));
    }
}