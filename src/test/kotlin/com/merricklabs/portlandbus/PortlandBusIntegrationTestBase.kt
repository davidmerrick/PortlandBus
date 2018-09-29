package com.merricklabs.portlandbus

import com.merricklabs.portlandbus.mocks.MockMyStopStorage
import com.merricklabs.portlandbus.mocks.MockTrimetClient
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import org.koin.standalone.StandAloneContext
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass

open class PortlandBusIntegrationTestBase {

    var config: PortlandBusConfig? = null

    @BeforeClass(groups = [INTEGRATION_GROUP])
    fun beforeClass() {
        StandAloneContext.startKoin(listOf(PortlandBusTestModule))
        config = PortlandBusConfig()
    }

    @AfterMethod(groups = [INTEGRATION_GROUP])
    fun afterMethod() {
        resetMocks()
    }

    private fun resetMocks() {
        MockTrimetClient().reset()
        MockMyStopStorage().reset()
    }
}