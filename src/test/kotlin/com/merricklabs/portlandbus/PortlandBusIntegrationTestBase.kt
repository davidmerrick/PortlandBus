package com.merricklabs.portlandbus

import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.storage.MyStopStorage
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.declareMock
import org.mockito.Mockito
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

open class PortlandBusIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    val trimetClient: TrimetClient by inject()
    val myStopStorage: MyStopStorage by inject()
    val config: PortlandBusConfig by inject()

    @BeforeMethod(groups = [INTEGRATION_GROUP])
    fun beforeMethod() {
        loadKoinModules(PortlandBusModule)
        declareMock<TrimetClient>()
        declareMock<MyStopStorage>()
    }

    @AfterMethod(groups = [INTEGRATION_GROUP])
    fun afterMethod() {
        stopKoin()
    }
}