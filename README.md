# DhanHQ-java: v1.0.1

The **unofficial** Java SDK for communicating with the [Dhan API](https://api.dhan.co/v2/).

DhanHQ-py Rest API is used to automate investing and trading. Execute orders in real time along with position management, live and historical data, tradebook and more with simple API collection.

> The implementation is nearing completion to what is in the API Docs. 
> The API for streaming/real-time data isn't yet fully incorporated and you shall have it sooner. 

## Documentation

- [DhanHQ Developer Kit](https://api.dhan.co/v2/)
- [DhanHQ API Documentation](https://dhanhq.co/docs/v2/)

## The Challenge

Stocking Market in India has been burgeoning on and on for over a decade now as part of India Growth story.
As consequence, we see a lot of Stock Broking companies cropping up as well.

Unfortunately but understandably (at least for me), most of stock broking platforms aren't stable, and performant on the server-side - the social media reeks of such reviews.
Many stock broking platforms including legacy banks don't even have SDKs and yet compete in screaming of being customer-obsessed.
And those that do have an SDK supporting one or two platforms, have a lot of room for improvement.  

This project is about filling one such gap in leveraging Dhan Stock broking platform APIs, by building SDKs for avid algorothimic traders.

## Motivation (The Challenge is the motivation!)

1. The primary goal of this SDK is to build an SDK that its users love to use 
by reducing the friction between the API documentation and code. This goal is achieved by adopting WYSIWYG (What You See Is What You Get) principle,
in bridging the gap between documentation and code.

    For example, as API consumer, you want to know get a **list of your holdings** associated with Dhan's Demat Account.
You first check it out in [API Docs](https://dhanhq.co/docs/v2/portfolio/) to see that it's hierarchy is like `API Docs > Portfolio > GET Holdings`.
The code implementation in SDK follows the same hierarchy like `DhanCore > Portfolio Endpoint > List<Holding>`. 
How does this affect you? Your code implementation is ridiculously brain friendly with fluent APIs like below:
    ```java
    List<Holding> currentHoldings = dhanCore // Think DhanCore akin to API Docs Page
            .getPortfolioEndpoint() // The Endpoint you see on the left-pane of the API docs
            .getCurrentHoldings(); // The intuitive method naming to make you productive
    ```

    The cheatsheet is as below:
   - DhanCore = Your API Doc Page 
   - <YourResource>Endpoint = Resource (you find on the left pane of your API doc-page) under which various actions are defined
   - <YourResource>Action = Action defined on the main section of your API document.

2. I want to design developer-first API design, that eases the life of an algorithmic developer. 
   You will find the API design of SDK refreshingly simple instead of being simplistic like that of other SDKs. 

3. Build Fluent APIs that hides technical complexities under the wraps of brings Domain Specific Language. 
   Even if you aren't a domain expert, you should become comfortable with the usage of this SDK.

## SDK API Cheatsheet

| Endpoint                     | Action                                                                                                                                                                                     | Returns  |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| `getOrderEndpoint()`         | `placeOrder(Order order)`                                                                                                                                                                  | `OrderStatusDTO` |
|                              | `placeOrder(Order order, String tag)`                                                                                                                                                      | `OrderStatusDTO` |
|                              | `placeOrder(Order order, String tag, boolean slice)`                                                                                                                                       | `OrderStatusDTO` |
|                              | `placeSliceOrder(Order order)`                                                                                                                                                             | `OrderStatusDTO` |
|                              | `placeSliceOrder(Order order, String tag)`                                                                                                                                                 | `OrderStatusDTO` |
|                              | `getCurrentOrders()`                                                                                                                                                                       | `List<Order>` |
|                              | `getOrderByID(String orderID)`                                                                                                                                                             | `Order`  |
|                              | `getOrderByCorrelationID(String correlationID)`                                                                                                                                            | `Order`  |
|                              | `modifyOrder(Order order)`                                                                                                                                                                 | `OrderStatusDTO` |
|                              | `cancelOrder(String orderID)`                                                                                                                                                              | `OrderStatusDTO` |
| `getForeverOrderEndpoint()`  | `placeForeverOrder(NewForeverOrderRequest foRequest)`                                                                                                                                      | `ForeverOrderResponse` |
|                              | `getAllForeverOrders()`                                                                                                                                                                    | `List<ForeverOrder>` |
|                              | `modifyForeverOrder(ModifyForeverOrderRequest mfoRequest)` | `ForeverOrderResponse` |
|                              | `cancelForeverOrder(String orderID)`                                                                                                                                                       | `OrderStatusDTO` |
| `getPortfolioEndpoint()`     | `getCurrentHoldings()`                                                                                                                                                                     | `List<Holding>` |
|                              | `getCurrentPositions()`                                                                                                                                                                    | `List<Position>` |
|                              | ```convertPosition(Position currentPosition, ProductType toProductType, int quantityToConvert)```                                                                                          | `DhanResponse` |
| `getFundsEndpoint()`         | `getFundLimitDetails()`                                                                                                                                                                    | `FundSummary` |
|                              | ```computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType, int quantity, ProductType productType, BigDecimal price)```                          | `Margin` |
|                              | ```computeMargin(String securityID, ExchangeSegment exchangeSegment, TransactionType transactionType, int quantity, ProductType productType, BigDecimal price, BigDecimal triggerPrice)``` | `Margin` |
| `getTraderControlEndpoint()` | `manageKillSwitch(KillSwitchStatus killSwitchStatus)`                                                                                                                                      | `String` |
| `getStatementEndpoint()`     | `getLedgerReport(LocalDate fromDate, LocalDate toDate)`                                                                                                                                    | `Ledger` |
| `getMarketQuotesEndpoint()`  | `getLTPFor(ExchangeSegmentSecurities exchangeSegmentSecurities)`                                                                                                                           | `ExchangeSegmentCandlesticksWrapper` |
|                              | `getOHLCFor(ExchangeSegmentSecurities exchangeSegmentSecurities)`                                                                                                                          | `ExchangeSegmentCandlesticksWrapper` |
|                              | `getQuoteFor(ExchangeSegmentSecurities exchangeSegmentSecurities)`                                                                                                                         | `ExchangeSegmentCandlesticksWrapper` |
| `getSecurityEndpoint()`      | `getEDISStatusOf(String isin)`                                                                                                                                                             | `EDISStatus` |
| | `generateTPIN()`                                                                                                                                                                           | `String` |
| | `openBrowserForTPin(String isin, int quantity, Exchange exchange, Segment segment, boolean bulk)`                                                                                          |          |
| | `createTempHtmlFile(String formHtml)`                                                                                                                                                      | `File`   |
| | `openInBrowser(File tempFile)`                                                                                                                                                             | |

## Cookbook

### The common starting point
```java
import co.dhan.api.DhanConnection;
import co.dhan.api.DhanCore;
        
DhanConnection dhanConnection = new DhanConnection("clientid","accessToken");
DhanCore dhanCore = new DhanCore(dhanConnection);
```

### On-demand data recipie

```java
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.ProductType;
import co.dhan.constant.TransactionType;

//Pre-requisite: See section "The common starting point".
//Pattern: dhanCore.getYourEndpoint().action();
FundSummary fundSummary = dhanCore.getFundsEndpoint().getFundLimitDetails();

List<Order> foreverOrders = dhanCore.getforeverOrderEndpoint().getAllForeverOrders();
List<Holding> currrentHoldings = dhanCore.getPortfolioEndpoint().getCurrentHoldings();
List<Position> currentPositions = dhanCore.getPortfolioEndpoint().getCurrentPositions();

// Convert select quantity of delivery order to intraday
List<Position> positions = dhanCore.getFundsEndpoint().getCurrentPositions();
dhanCore.getPortfolioEndpoint()
        .convertPosition(positions.get(0), ProductType.INTRADAY, 10);

Margin margin = dhanCore.getFundsEndpoint()
        .computeMargin("securityId",
                        ExchangeSegment.NSE_EQ,
                        TransactionType.BUY,
                        100,
                        ProductType.CNC,
                        BigDecimalUtils.toBigDecimal(price),
                        BigDecimalUtils.toBigDecimal(triggerPrice));
```

### Live Market Feed Usage

```java

import co.dhan.api.stream.LiveMarketfeedListener;
import co.dhan.constant.ExchangeSegment;
import co.dhan.dto.Instrument;
import co.dhan.dto.InstrumentAction;

import java.util.ArrayList;

LiveMarketfeed liveMarketfeed = dhanCore.getLiveMarketFeed();

List<Instrument> subscriptionsToTicks = new ArrayList<>();
subscriptionsToTicks.add(new Instrument(ExchangeSegment.NSE_EQ, "1111"));
subscriptionsToTicks.add(new Instrument(ExchangeSegment.NSE_EQ, "2222"));
subscriptionsToTicks.add(new Instrument(ExchangeSegment.NSE_EQ, "3333"));
subscriptionsToTicks.add(new Instrument(ExchangeSegment.NSE_EQ, "4444"));

LiveMarketfeedListener liveMarketfeedListener = new LiveMarketfeedListener() {
   public void onConnection() {
      liveMarketfeed.command(subscriptionsToTicks, FeedRequestCode.SUBSCRIBE_TICK);
   }

   public void onError(Exception e) {
      System.out.println(e);
   }

   public void onTickerArrival(LiveTicker liveTicker) {
      System.out.println(liveTicker);
   }

   public void onPrevCloseArrival(LivePrevClose livePrevClose) {
      System.out.println(livePrevClose);
   }

   public void onQuoteArrival(LiveQuote liveQuote) {
      System.out.println(liveQuote);
   }

   public void onOIArrival(LiveOI liveOI) {
      System.out.println(liveOI);
   }

   public void onQuoteMaxArrival(LiveQuoteMax liveQuoteMax) {
      System.out.println(liveQuoteMax);
   }
};
liveMarketfeed.connect(liveMarketfeedListener);

liveMarketfeed.command(new Instrument(ExchangeSegment.NSE_EQ, "1111"), FeedRequestCode.UNSUBSCRIBE_TICK);
liveMarketfeed.command(new Instrument(ExchangeSegment.NSE_EQ, "234567"), FeedRequestCode.SUBSCRIBE_QUOTE);
liveMarketfeed.command(new Instrument(ExchangeSegment.NSE_EQ, "345678"), FeedRequestCode.SUBSCRIBE_FULL_QUOTE);


// When you want to disconnect:
// ... (When your application is shutting down or when the user explicitly disconnects)
liveMarketfeed.disconnect();
```

### Live 20-Level Market Depth Usage

```java
import co.dhan.api.stream.LiveMarketDepth;
import co.dhan.api.stream.listener.LiveMarketDepthListener;
import co.dhan.constant.FeedRequestCode;
import co.dhan.http.DhanAPIException;

LiveMarketDepth liveMarketDepth = dhanCore.getLiveMarketDepth();

List<Instrument> subscriptionsToTicks = new ArrayList<>();
subscriptionsToTicks.

add(new Instrument(ExchangeSegment.NSE_EQ, "1111"));
        subscriptionsToTicks.

add(new Instrument(ExchangeSegment.NSE_EQ, "2222"));


LiveMarketDepthListener depthListener = new LiveMarketDepthListener() {
    @Override
    public void onConnection() {
        System.out.println("LiveMarketDepthListener.onConnection() : ");
        liveMarketDepth.command(subscriptionsToTicks, FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);
    }

    @Override
    public void onTermination(DhanAPIException e) {
        System.out.println("LiveMarketDepthListener.onTermination() : " + e);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("LiveMarketDepthListener.onError() : " + e);
    }

    @Override
    public void onBidsArrival(LiveDepth liveDepth) {
        System.out.println("LiveMarketDepthListener.onBidsArrival(..), size " + liveDepth.getBuy().size()
                + " for securityID " + liveDepth.getSecurityID());
        System.out.println(liveDepth.getBuy());
    }

    @Override
    public void onAsksArrival(LiveDepth liveDepth) {
        System.out.println("LiveMarketDepthListener.onAsksArrival(..), size " + liveDepth.getSell().size()
                + " for securityID " + liveDepth.getSecurityID());
        System.out.println(liveDepth.getSell());
    }
};

liveMarketDepth.

connect();
liveMarketfeed.

command(new Instrument(ExchangeSegment.NSE_EQ, "456789"),FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);
        liveMarketDepth.

command();
liveMarketDepth.

disconnect();
```


## Dev Setup Instructions for Code Contribution

- Create `config.properties` in project root for it to be sibling of `pom.xml` 
   and define a property like `dev.env=true` to ensure `dev` profile of maven is activated by default.
- If you are using IntelliJ IDE, choose Maven Icon on the right pane, 
  select `dev` profile optionally and execute clean and test lifecycle methods.

## Publish Checklist

    [] Zero Errors in Automated Tests 
    [] Version Update (`pom.xml` and `Readme.md`)

## Want to contribute?

If you are an Stock Trader doing Algorithmic Trading or wanting to do one, this is your go to SDK for your productivity at work.

There are a number of ways you can contribute to this project:
- As end-user file [issues](https://github.com/karthiks/DhanHQ-java/issues), sharing bugs, if any, you encounter in using this SDK.
- As end-user file [issues](https://github.com/karthiks/DhanHQ-java/issues), seeking new features you'd wish to see. 
  But remember, this SDK is your vehicle to simplify the complexity of the vanilla server-side API. 
  If you are in doubt, check out what it takes to use Live feed from the API using the Websockets from most Stock Broking APIs in India.
- This ain't a perfect piece and so would love to see you contributing [PRs](https://github.com/karthiks/DhanHQ-java/pulls) as collaborative endeavor to make the lives of Algorithimic traders better and better. 
