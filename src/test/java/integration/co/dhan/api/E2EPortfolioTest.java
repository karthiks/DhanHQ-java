package co.dhan.api;

import co.dhan.http.DhanAPIException;
import co.dhan.dto.Holding;
import org.junit.jupiter.api.Test;

import java.util.List;

public class E2EPortfolioTest extends E2EDhanTestRoot {

    @Test
    public void getCurrentHoldingsSuccessfully() throws DhanAPIException {
        List<Holding> currentHoldings = dhanCore.getPortfolioEndpoint().getCurrentHoldings();
        System.out.println("--Order List--");
        currentHoldings.forEach(System.out::println);
    }
}
