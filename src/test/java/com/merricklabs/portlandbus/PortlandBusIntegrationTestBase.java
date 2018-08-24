package com.merricklabs.portlandbus;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeClass;

import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;

public class PortlandBusIntegrationTestBase {

    protected Injector injector;

    @BeforeClass(groups = INTEGRATION_GROUP)
    public void beforeClass(){
        injector = Guice.createInjector(new PortlandBusTestModule());
    }

}
