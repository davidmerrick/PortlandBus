package com.merricklabs.portlandbus

import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.declareMock
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass

open class PortlandBusIntegrationTestBase : KoinTest {

    val trimetClient: TrimetClient by inject()
    val config: PortlandBusConfig by inject()

    @BeforeClass(groups = [INTEGRATION_GROUP])
    fun beforeClass() {
        loadKoinModules(PortlandBusModule)
        declareMock<TrimetClient>()
    }

    @AfterMethod(groups = [INTEGRATION_GROUP])
    fun afterMethod() {
        stopKoin()
    }
}