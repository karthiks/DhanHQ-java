package co.dhan.api;

import co.dhan.api.ondemand.*;
import co.dhan.api.stream.LiveMarketDepth;
import co.dhan.api.stream.LiveMarketFeed;
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
    private final MarketQuotesEndpoint marketQuotesEndpoint;
    private final LiveMarketFeed liveMarketFeed;
    private final LiveMarketDepth liveMarketDepth;

    public DhanCore(DhanConnection dhanConnection) {
        orderEndpoint = new OrderEndpoint(dhanConnection);
        foreverOrderEndpoint = new ForeverOrderEndpoint(dhanConnection);
        portfolioEndpoint = new PortfolioEndpoint(dhanConnection);
        fundsEndpoint = new FundsEndpoint(dhanConnection);
        traderControlEndpoint = new TraderControlEndpoint(dhanConnection);
        statementEndpoint = new StatementEndpoint(dhanConnection);
        securityEndpoint = new SecurityEndpoint(dhanConnection);
        marketQuotesEndpoint = new MarketQuotesEndpoint(dhanConnection);
        liveMarketFeed = new LiveMarketFeed(dhanConnection);
        liveMarketDepth = new LiveMarketDepth(dhanConnection);
    }
}
