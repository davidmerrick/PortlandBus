package com.merricklabs.portlandbus.handlers;

import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;

@Slf4j
public class GetArrivalsIntentHandlerTest extends PortlandBusIntegrationTestBase {

    @Test(groups = INTEGRATION_GROUP)
    public void getNextArrivals(){
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
    }
}