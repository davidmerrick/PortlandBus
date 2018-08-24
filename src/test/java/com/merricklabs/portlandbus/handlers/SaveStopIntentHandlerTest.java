package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.google.common.collect.ImmutableMap;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase;
import com.merricklabs.portlandbus.testutil.HandlerInputBuilder;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.Test;

import static com.merricklabs.portlandbus.constants.PortlandBusIntents.SAVE_STOP_INTENT;
import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;
import static com.merricklabs.portlandbus.testutil.TestData.STOP_ID;
import static com.merricklabs.portlandbus.testutil.TestData.USER_ID;
import static org.testng.Assert.assertTrue;

public class SaveStopIntentHandlerTest extends PortlandBusIntegrationTestBase {

    @Test(groups = INTEGRATION_GROUP)
    public void saveStop() {
        HandlerInput input = getValidInput(USER_ID, String.valueOf(STOP_ID));
        SaveStopIntentHandler handler = injector.getInstance(SaveStopIntentHandler.class);
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());

        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Saved stop"));
        assertTrue(speechText.contains(String.valueOf(STOP_ID)));
    }

    @Test(groups = INTEGRATION_GROUP)
    public void stopNotSpecified() {
        HandlerInput input = getInvalidInput(USER_ID);
        SaveStopIntentHandler handler = injector.getInstance(SaveStopIntentHandler.class);
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());

        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Please specify a stop to save."));
    }

    private HandlerInput getValidInput(String userId, String stopId) {
        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        return HandlerInputBuilder.builder()
                .config(alexaConfig)
                .slots(buildSlots(String.valueOf(stopId), alexaConfig))
                .intentName(SAVE_STOP_INTENT)
                .userId(userId)
                .build()
                .getHandlerInput();
    }

    private HandlerInput getInvalidInput(String userId) {
        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        return HandlerInputBuilder.builder()
                .config(alexaConfig)
                .intentName(SAVE_STOP_INTENT)
                .slots(ImmutableMap.of())
                .userId(userId)
                .build()
                .getHandlerInput();
    }

    private static Map<String, Slot> buildSlots(String stopIdString, PortlandBusConfig.Alexa alexaConfig) {
        return ImmutableMap.of(
                alexaConfig.getStopIdSlot(),
                Slot.builder()
                        .withName(alexaConfig.getStopIdSlot())
                        .withConfirmationStatus(SlotConfirmationStatus.NONE)
                        .withValue(stopIdString)
                        .build()
        );
    }
}
