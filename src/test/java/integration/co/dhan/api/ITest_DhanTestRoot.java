package co.dhan.api;

import co.dhan.api.ondemand.ITest_OrderEndpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Properties;

public abstract class ITest_DhanTestRoot {
    protected static Properties ConfigurationProperties;
    protected DhanConnection dhanConnection;
    protected DhanCore dhanCore;


    @BeforeAll
    static void setUpOnce() throws IOException {
        ConfigurationProperties = new Properties();
        ConfigurationProperties
                .load(ITest_OrderEndpoint.class
                        .getClassLoader()
                        .getResourceAsStream("integration-test.properties")
                );
    }

    @BeforeEach
    void setUp() {
        dhanConnection = new DhanConnection(ConfigurationProperties.getProperty("dhan.clientID"),
                ConfigurationProperties.getProperty("dhan.accessToken"));
        dhanCore = new DhanCore(dhanConnection);
    }

}
