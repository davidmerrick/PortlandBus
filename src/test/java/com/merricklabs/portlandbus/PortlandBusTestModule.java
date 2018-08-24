package com.merricklabs.portlandbus;

import com.google.inject.AbstractModule;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.mocks.MockMyStopStorage;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;
import com.merricklabs.portlandbus.storage.MyStopStorage;

public class PortlandBusTestModule extends AbstractModule {

    @Override
    protected void configure(){
        bind(TrimetClient.class).to(MockTrimetClient.class);
        bind(MyStopStorage.class).to(MockMyStopStorage.class);
    }
}
