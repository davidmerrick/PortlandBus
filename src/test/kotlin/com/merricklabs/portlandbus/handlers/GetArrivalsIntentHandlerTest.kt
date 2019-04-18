package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.model.Slot
import com.amazon.ask.model.SlotConfirmationStatus
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase
import com.merricklabs.portlandbus.constants.PortlandBusIntents.GET_ARRIVALS_INTENT
import com.merricklabs.portlandbus.testutil.MockTrimetUtils
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import com.merricklabs.portlandbus.testutil.TestData.STOP_ID
import com.merricklabs.portlandbus.testutil.TestData.USER_ID
import com.merricklabs.portlandbus.testutil.TestHandlerInput
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class GetArrivalsIntentHandlerTest : PortlandBusIntegrationTestBase() {

    private val getArrivalsIntentHandler: GetArrivalsIntentHandler by inject()

    @Test(groups = [INTEGRATION_GROUP])
    fun `get next arrivals`() {
        Mockito.`when`(trimetClient.getArrivalsForStop(STOP_ID)).thenReturn(MockTrimetUtils.getDummyArrivals(STOP_ID, 5))
        val input = getValidInput(STOP_ID)
        val responseOptional = getArrivalsIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)
        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("Next arrivals at stop"))
        assertTrue(speechText.contains(STOP_ID.toString()))
        assertTrue(speechText.contains(", and bus"))
    }

    @Test(groups = [INTEGRATION_GROUP])
    fun `test with invalid input`() {
        val input = invalidInput
        val responseOptional = getArrivalsIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)
        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("Sorry, there was a problem getting arrivals for that stop."))
    }

    private fun getValidInput(stopId: Int): HandlerInput {
        val alexaConfig = config.alexa
        return TestHandlerInput.Builder()
                .slots(buildSlots(stopId.toString(), alexaConfig))
                .userId(USER_ID)
                .intentName(GET_ARRIVALS_INTENT)
                .build()
                .handlerInput
    }

    private fun buildSlots(stopIdString: String, alexaConfig: PortlandBusConfig.Alexa): Map<String, Slot> {
        return mapOf(alexaConfig.stopIdSlot to
                Slot.builder()
                        .withName(alexaConfig.stopIdSlot)
                        .withConfirmationStatus(SlotConfirmationStatus.NONE)
                        .withValue(stopIdString)
                        .build())
    }

    private val invalidInput: HandlerInput
        get() {
            val alexaConfig = config.alexa
            return TestHandlerInput.Builder()
                    .slots(buildSlots("", alexaConfig))
                    .userId(USER_ID)
                    .intentName(GET_ARRIVALS_INTENT)
                    .build()
                    .handlerInput
        }
}