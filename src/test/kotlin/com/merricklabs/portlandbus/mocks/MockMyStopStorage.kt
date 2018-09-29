package com.merricklabs.portlandbus.mocks

import com.merricklabs.portlandbus.storage.MyStopStorage
import java.util.OptionalInt

class MockMyStopStorage : MyStopStorage, TestMock {

    private var myStop = OptionalInt.empty()

    override fun saveStop(userId: String, stopId: Int) {
        myStop = OptionalInt.of(stopId)
    }

    override fun queryStopId(userId: String): OptionalInt {
        return myStop
    }

    override fun reset() {
        myStop = OptionalInt.empty()
    }
}