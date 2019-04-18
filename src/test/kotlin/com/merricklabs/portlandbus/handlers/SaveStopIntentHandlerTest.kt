package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.model.Slot
import com.amazon.ask.model.SlotConfirmationStatus
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase
import com.merricklabs.portlandbus.constants.PortlandBusIntents.SAVE_STOP_INTENT
import com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP
import com.merricklabs.portlandbus.testutil.TestData.STOP_ID
import com.merricklabs.portlandbus.testutil.TestData.USER_ID
import com.merricklabs.portlandbus.testutil.TestHandlerInput
import org.koin.standalone.inject
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class SaveStopIntentHandlerTest : PortlandBusIntegrationTestBase() {

    private val saveStopIntentHandler: SaveStopIntentHandler by inject()

    @Test(groups = [INTEGRATION_GROUP])
    fun `save stop`() {
        val input = getValidInput(USER_ID, STOP_ID.toString())
        val responseOptional = saveStopIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)

        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("Saved stop"))
        assertTrue(speechText.contains(STOP_ID.toString()))
    }

    @Test(groups = [INTEGRATION_GROUP])
    fun `stop not specified`() {
        val input = getInvalidInput(USER_ID)
        val responseOptional = saveStopIntentHandler.handle(input)
        assertTrue(responseOptional.isPresent)

        val speechText = responseOptional.get().outputSpeech.toString()
        assertTrue(speechText.contains("Please specify a stop to save."))
    }

    private fun getValidInput(userId: String, stopId: String): HandlerInput {
        return TestHandlerInput.Builder()
                .slots(buildSlots(stopId, config.alexa))
                .intentName(SAVE_STOP_INTENT)
                .userId(userId)
                .build()
                .handlerInput
    }

    private fun getInvalidInput(userId: String): HandlerInput {
        return TestHandlerInput.Builder()
                .intentName(SAVE_STOP_INTENT)
                .slots(mapOf())
                .userId(userId)
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
}