package com.merricklabs.portlandbus;

import com.google.inject.AbstractModule;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;

public class PortlandBusTestModule extends AbstractModule {

    @Override
    protected void configure(){
        bind(TrimetClient.class).to(MockTrimetClient.class);
    }
}
