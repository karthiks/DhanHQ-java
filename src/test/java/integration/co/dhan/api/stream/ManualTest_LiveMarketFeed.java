package co.dhan.api.stream;

import co.dhan.api.DhanConnection;
import co.dhan.api.DhanCore;
import co.dhan.api.ondemand.E2EOrderEndpointTest;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedRequestCode;
import co.dhan.dto.*;
import co.dhan.http.DhanAPIException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ManualTest_LiveMarketFeed {
//    private static final String SERVER_URL = "wss://stockmarket.example.com/ws"; // Replace with your server URL

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(E2EOrderEndpointTest.class
                    .getClassLoader()
                    .getResourceAsStream("integration-test.properties")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String clientId = properties.getProperty("dhan.clientID");
        String accessToken = properties.getProperty("dhan.accessToken");
        DhanConnection dhanConnection = new DhanConnection(clientId,accessToken);
        DhanCore dhanCore = new DhanCore(dhanConnection);

        LiveMarketFeed liveMarketfeed = dhanCore.getLiveMarketFeed();

        LiveMarketFeedListener liveMarketfeedListener = new LiveMarketFeedListener() {
            public void onConnection() {
                System.out.println("LiveMarketFeedListener.onConnection():");
                Instrument iNTPCGreen = new Instrument(ExchangeSegment.NSE_EQ, "27176");
                Instrument iABB = new Instrument(ExchangeSegment.BSE_EQ, "500002");
//                Instrument iAdaniEnergy = new Instrument(ExchangeSegment.NSE_EQ, "10217");
                Instrument iEnviroInfra = new Instrument(ExchangeSegment.NSE_EQ, "27213");
//                subscriptionsToTicks.add(iAdaniEnergy);
//                subscriptionsToTicks.add(iEnviroInfra);
                liveMarketfeed.command(List.of(iNTPCGreen), FeedRequestCode.SUBSCRIBE_TICK);
                liveMarketfeed.command(List.of(iABB), FeedRequestCode.SUBSCRIBE_QUOTE);
                liveMarketfeed.command(List.of(iEnviroInfra), FeedRequestCode.SUBSCRIBE_FULL_QUOTE);
            }

            public void onError(Exception e) {
                System.out.println("LiveMarketFeedListener.onError():");
                System.out.println(e);
            }

            public void onTermination(DhanAPIException e) {
                System.out.println("LiveMarketFeedListener.onTermination():");
                System.out.println(e);
            }

            public void onTickerArrival(LiveTicker liveTicker) {
                System.out.println("onTickerArrival() : " + liveTicker);
            }

            public void onPrevCloseArrival(LivePrevClose livePrevClose) {
                System.out.println("onPrevCloseArrival() : " + livePrevClose);
            }

            public void onQuoteArrival(LiveQuote liveQuote) {
                System.out.println("onQuoteArrival() : " + liveQuote);
            }

            public void onOIArrival(LiveOI liveOI) {
                System.out.println("onOIArrival() : " + liveOI);
            }

            public void onQuoteMaxArrival(LiveQuoteMax liveQuoteMax) {
                System.out.println("onQuoteMaxArrival() : " + liveQuoteMax);
            }
        };
        liveMarketfeed.connect(liveMarketfeedListener);
        try {
            Thread.sleep(Duration.ofSeconds(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        liveMarketfeed.disconnect();
    }
}