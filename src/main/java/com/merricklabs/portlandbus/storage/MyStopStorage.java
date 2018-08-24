package com.merricklabs.portlandbus.storage;

import java.util.OptionalInt;

public interface MyStopStorage {
    public void saveStop(String userId, int stopId);

    public OptionalInt queryStopId(String userId);
}
