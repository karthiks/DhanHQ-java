package co.dhan.api;

import co.dhan.api.ondemand.E2EOrderEndpointTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Properties;

public abstract class E2EDhanTestRoot {
    protected static Properties ConfigurationProperties;
    protected DhanConnection dhanConnection;
    protected DhanCore dhanCore;


    @BeforeAll
    static void setUpOnce() throws IOException {
        ConfigurationProperties = new Properties();
        ConfigurationProperties
                .load(E2EOrderEndpointTest.class
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
