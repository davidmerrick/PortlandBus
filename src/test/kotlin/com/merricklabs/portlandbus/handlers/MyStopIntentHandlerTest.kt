package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase
import com.merricklabs.portlandbus.constants.PortlandBusIntents.MY_STOP_INTENT
import com.merricklabs.portlandbus.testutil.MockTrimetUtils
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import com.merricklabs.portlandbus.testutil.TestData.STOP_ID
import com.merricklabs.portlandbus.testutil.TestData.USER_ID
import com.merricklabs.portlandbus.testutil.TestHandlerInput
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.util.OptionalInt

class MyStopIntentHandlerTest : PortlandBusIntegrationTestBase() {

    private val myStopIntentHandler: MyStopIntentHandler by inject()

    @Test(groups = [INTEGRATION_GROUP])
    fun `user has stop saved`() {
        // Initialize mocks
        Mockito.`when`(trimetClient.getArrivalsForStop(STOP_ID)).thenReturn(MockTrimetUtils.getDummyArrivals(STOP_ID, 5))
        Mockito.`when`(myStopStorage.queryStopId(USER_ID)).thenReturn(OptionalInt.of(STOP_ID))

        val input = getInput(USER_ID)
        val responseOptional = myStopIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)

        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("Next arrivals at stop"))
        assertTrue(speechText.contains(STOP_ID.toString()))
        assertTrue(speechText.contains(", and bus"))
    }

    @Test(groups = [INTEGRATION_GROUP])
    fun `user does not have stop saved`() {
        Mockito.`when`(myStopStorage.queryStopId(any())).thenReturn(OptionalInt.empty())
        val input = getInput(USER_ID)
        val responseOptional = myStopIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)

        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("You haven't saved a stop yet. Please save a stop first."))
    }

    private fun getInput(userId: String): HandlerInput {
        return TestHandlerInput.Builder()
                .intentName(MY_STOP_INTENT)
                .userId(userId)
                .build()
                .handlerInput
    }
}