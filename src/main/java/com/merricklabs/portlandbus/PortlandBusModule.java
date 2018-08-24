package com.merricklabs.portlandbus;

import com.google.inject.AbstractModule;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.external.trimet.TrimetClientImpl;
import com.merricklabs.portlandbus.storage.MyStopStorage;
import com.merricklabs.portlandbus.storage.MyStopStorageImpl;

public class PortlandBusModule extends AbstractModule {

    @Override
    protected void configure(){
        bind(TrimetClient.class).to(TrimetClientImpl.class);
        bind(MyStopStorage.class).to(MyStopStorageImpl.class);
    }
}
