package co.dhan.api.stream;

import co.dhan.api.DhanConnection;
import co.dhan.api.DhanCore;
import co.dhan.api.ondemand.E2EOrderEndpointTest;
import co.dhan.constant.ExchangeSegment;
import co.dhan.constant.FeedRequestCode;
import co.dhan.dto.Instrument;
import co.dhan.dto.LiveDepth;
import co.dhan.http.DhanAPIException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ManualTest_LiveDepthFeed {
//    private static final String SERVER_URL = "wss://stockmarket.example.com/ws"; // Replace with your server URL
    private static Instrument instrument = new Instrument(ExchangeSegment.NSE_EQ, "1");

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
                log.debug("LiveMarketDepthListener.onConnection():");
//                Instrument iNTPCGreen = new Instrument(ExchangeSegment.NSE_EQ, "27176");
//                Instrument iGoldstar = new Instrument(ExchangeSegment.NSE_EQ, "1");
//                Instrument iAban = new Instrument(ExchangeSegment.NSE_EQ, "10");
                Instrument iEnviroInfra = new Instrument(ExchangeSegment.NSE_EQ, "25412");
//                subscriptionsToTicks.add(iAdaniEnergy);
//                subscriptionsToTicks.add(iEnviroInfra);
                liveMarketDepth.command(
                        List.of(instrument),
                        FeedRequestCode.SUBSCRIBE_20_LEVEL_DEPTH);
            }

            public void onError(Exception e) {
                log.debug("LiveMarketDepthListener.onError():");
                log.debug(e.getMessage());
            }

            @Override
            public void onBidsArrival(LiveDepth liveDepth) {
                log.debug("LiveMarketDepthListener.onBidsArrival(..), size " + liveDepth.getBuy().size()
                + " for securityID " + liveDepth.getSecurityID());
                log.debug(liveDepth.getBuy().toString());
            }

            @Override
            public void onAsksArrival(LiveDepth liveDepth) {
                log.debug("LiveMarketDepthListener.onAsksArrival(..), size " + liveDepth.getSell().size()
                        + " for securityID " + liveDepth.getSecurityID());
                log.debug(liveDepth.getSell().toString());
            }

            public void onTermination(DhanAPIException e) {
                log.debug("LiveMarketDepthListener.onTermination():");
                log.debug(e.getMessage());
            }

        };
        liveMarketDepth.connect(liveMarketDepthListener);
/*        try {
            Thread.sleep(Duration.ofSeconds(10));
            liveMarketDepth.command(List.of(instrument),FeedRequestCode.UNSUBSCRIBE_20_LEVEL_DEPTH);
            Thread.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        liveMarketDepth.disconnect();*/
    }
}