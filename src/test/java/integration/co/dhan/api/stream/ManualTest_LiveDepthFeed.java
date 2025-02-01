package co.dhan.api.stream;

import co.dhan.api.DhanConnection;
import co.dhan.api.DhanCore;
import co.dhan.api.ondemand.E2EOrderEndpointTest;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedRequestCode;
import co.dhan.dto.Instrument;
import co.dhan.dto.LiveDepth;
import co.dhan.http.DhanAPIException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ManualTest_LiveDepthFeed {
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

        LiveMarketDepth liveMarketDepth = dhanCore.getLiveMarketDepth();

        LiveMarketDepthListener liveMarketDepthListener = new LiveMarketDepthListener() {
            public void onConnection() {
                System.out.println("LiveMarketDepthListener.onConnection():");
//                Instrument iNTPCGreen = new Instrument(ExchangeSegment.NSE_EQ, "27176");
//                Instrument iGoldstar = new Instrument(ExchangeSegment.NSE_EQ, "1");
//                Instrument iAban = new Instrument(ExchangeSegment.NSE_EQ, "10");
                Instrument iEnviroInfra = new Instrument(ExchangeSegment.NSE_EQ, "25412");
//                subscriptionsToTicks.add(iAdaniEnergy);
//                subscriptionsToTicks.add(iEnviroInfra);
                liveMarketDepth.command(
                        List.of(new Instrument(ExchangeSegment.NSE_EQ, "10181")),
                        FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);
            }

            public void onError(Exception e) {
                System.out.println("LiveMarketDepthListener.onError():");
                System.out.println(e);
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

            public void onTermination(DhanAPIException e) {
                System.out.println("LiveMarketDepthListener.onTermination():");
                System.out.println(e);
            }

        };
        liveMarketDepth.connect(liveMarketDepthListener);
        try {
            Thread.sleep(Duration.ofSeconds(15));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        liveMarketDepth.disconnect();
    }
}