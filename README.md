# DhanHQ-java: v0.1.0-SNAPSHOT

The **unofficial** Java SDK for communicating with the [Dhan API](https://api.dhan.co/v2/).

DhanHQ-py Rest API is used to automate investing and trading. Execute orders in real time along with position management, live and historical data, tradebook and more with simple API collection.

> The implementation is nearing completion to what is in the API Docs. 
> The API for streaming/real-time data isn't yet incorporated and you shall have it sooner. 

## Documentation

- [DhanHQ Developer Kit](https://api.dhan.co/v2/)
- [DhanHQ API Documentation](https://dhanhq.co/docs/v2/)

## Motivation

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

4. ***Last but not the least, no developer should be asked to go through burnout hell at work by infringing in their personal lives.
   What developers do in their personal lives - staring, sharing or caring - is best left for them to decide. 
   The SDK is built consciously to make the life of a developer - productive instead of being just busy. 
   May you work well, live healthier and nurture others around you, for a better tomorrow.*** 

## Cookbook

### The common starting point
```java
import co.dhan.api.DhanConnection;
import co.dhan.api.DhanCore;
        
DhanConnection dhanConnection = new DhanConnection("clientid","accessToken");
DhanCore dhanCore = new DhanCore(dhanConnection);
```
### Ondemand data recipie
### Ondemand data recipie
```java
//Pre-requisite: See section "The common starting point".
//Pattern: dhanCore.getYourEndpoint().action();
var result = dhanCore.getPortfolioEndpoint().getCurrentHoldings();
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
liveMarketfeed.command(new Instrument(ExchangeSegment.NSE_EQ, "456789"), FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);


// When you want to disconnect:
// ... (When your application is shutting down or when the user explicitly disconnects)
liveMarketfeed.disconnect();
```

## Want to contribute?

Feel free to file your issues or send in your PRs for active collaboration to make this even sweeter for its users.