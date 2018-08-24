package com.merricklabs.portlandbus;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;

public class PortlandBusIntegrationTestBase {

    protected Injector injector;

    @BeforeClass(groups = INTEGRATION_GROUP)
    public void beforeClass(){
        injector = Guice.createInjector(new PortlandBusTestModule());
    }

    @AfterMethod(groups = INTEGRATION_GROUP)
    public void afterMethod() {
        resetMocks();
    }

    private void resetMocks() {
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
        mockTrimetClient.reset();
    }
}
