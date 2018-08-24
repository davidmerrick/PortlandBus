package com.merricklabs.portlandbus.mocks;

import com.google.inject.Singleton;
import com.merricklabs.portlandbus.storage.MyStopStorage;
import java.util.OptionalInt;

@Singleton
public class MockMyStopStorage implements MyStopStorage, TestMock {

    OptionalInt myStop = OptionalInt.empty();

    @Override
    public void saveStop(String userId, int stopId) {
        myStop = OptionalInt.of(stopId);
    }

    @Override
    public OptionalInt queryStopId(String userId) {
        return myStop;
    }

    @Override
    public void reset() {
        myStop = OptionalInt.empty();
    }
}
