package co.dhan.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Properties;

public abstract class E2EDhanTestRoot {
    protected static Properties ConfigurationProperties;
    protected DhanContext dhanContext;
    protected DhanCore dhanCore;


    @BeforeAll
    static void setUpOnce() throws IOException {
        ConfigurationProperties = new Properties();
        ConfigurationProperties
                .load(E2EOrdersTest.class
                        .getClassLoader()
                        .getResourceAsStream("integration-test.properties")
                );
    }

    @BeforeEach
    void setUp() {
        dhanContext = new DhanContext("", "");
        dhanCore = new DhanCore(dhanContext);
    }

}
