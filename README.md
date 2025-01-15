# DhanHQ-java: v0.1.0-SNAPSHOT

The **unofficial** Java SDK for communicating with the [Dhan API](https://api.dhan.co/v2/).

DhanHQ-py Rest API is used to automate investing and trading. Execute orders in real time along with position management, live and historical data, tradebook and more with simple API collection.

> The implementation is nearing completion to what is in the API Docs. 
> The API for streaming/real-time data isn't yet incorporated and you shall have it sooner. 

## Documentation

- [DhanHQ Developer Kit](https://api.dhan.co/v2/)
- [DhanHQ API Documentation](https://dhanhq.co/docs/v2/)

## Motivation

The primary goal of this SDK is to build an SDK that its users love to use 
by reducing the friction between the API documentation and code. 

This goal is achieved by adopting WYSIWYG (What You See Is What You Get) principle,
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

This also means you become domain expert in what you do as you use our SDK to build your Algorothimic Trading applications.

## Want to contribute?

Feel free to file your issues or send in your PRs for active collaboration to make this even sweeter for its users.