package co.dhan.api;

import co.dhan.api.ondemand.*;
import lombok.Getter;

@Getter
public class DhanCore {

    private final OrderEndpoint orderEndpoint;
    private final ForeverOrderEndpoint foreverOrderEndpoint;
    private final PortfolioEndpoint portfolioEndpoint;
    private final FundsEndpoint fundsEndpoint;
    private final TraderControlEndpoint traderControlEndpoint;
    private final StatementEndpoint statementEndpoint;
    private final SecurityEndpoint securityEndpoint;

    public DhanCore(DhanContext dhanContext) {
        orderEndpoint = new OrderEndpoint(dhanContext);
        foreverOrderEndpoint = new ForeverOrderEndpoint(dhanContext);
        portfolioEndpoint = new PortfolioEndpoint(dhanContext);
        fundsEndpoint = new FundsEndpoint(dhanContext);
        traderControlEndpoint = new TraderControlEndpoint(dhanContext);
        statementEndpoint = new StatementEndpoint(dhanContext);
        securityEndpoint = new SecurityEndpoint(dhanContext);
    }
}
