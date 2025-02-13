package co.dhan.api.ondemand;

import co.dhan.api.ITest_DhanTestRoot;
import co.dhan.dto.Holding;
import co.dhan.http.DhanAPIException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ITest_PortfolioEndpoint extends ITest_DhanTestRoot {

    @Test
    public void getCurrentHoldingsSuccessfully() throws DhanAPIException {
        List<Holding> currentHoldings = dhanCore.getPortfolioEndpoint().getCurrentHoldings();
        assertThat(currentHoldings).isNotNull();
    }
}
