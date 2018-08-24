package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.google.common.collect.ImmutableMap;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase;
import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;
import com.merricklabs.portlandbus.testutil.HandlerInputBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.merricklabs.portlandbus.constants.PortlandBusIntents.GET_ARRIVALS_INTENT;
import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;
import static com.merricklabs.portlandbus.testutil.TestData.STOP_ID;
import static com.merricklabs.portlandbus.testutil.TestData.USER_ID;
import static org.testng.Assert.assertTrue;

@Slf4j
public class GetArrivalsIntentHandlerTest extends PortlandBusIntegrationTestBase {

    @Test(groups = INTEGRATION_GROUP)
    public void getNextArrivals(){
        // Initialize mock trimet client
        List<Arrival> dummyArrivals = MockTrimetClient.getDummyArrivals(STOP_ID, 5);
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
        mockTrimetClient.setArrivals(dummyArrivals);

        HandlerInput input = getValidInput(STOP_ID);
        GetArrivalsIntentHandler handler = injector.getInstance(GetArrivalsIntentHandler.class);
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());
        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Next arrivals at stop"));
        assertTrue(speechText.contains(String.valueOf(STOP_ID)));
        assertTrue(speechText.contains(", and bus"));
    }

    @Test(groups = INTEGRATION_GROUP)
    public void testWithInvalidInput() {
        HandlerInput input = getInvalidInput();
        GetArrivalsIntentHandler handler = injector.getInstance(GetArrivalsIntentHandler.class);
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());
        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Sorry, there was a problem getting arrivals for that stop."));
    }

    private HandlerInput getValidInput(int stopId) {
        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        return HandlerInputBuilder.builder()
                .config(alexaConfig)
                .slots(buildSlots(String.valueOf(stopId), alexaConfig))
                .intentName(GET_ARRIVALS_INTENT)
                .userId(USER_ID)
                .build()
                .getHandlerInput();
    }

    private HandlerInput getInvalidInput() {
        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        return HandlerInputBuilder.builder()
                .config(alexaConfig)
                .slots(buildSlots("", alexaConfig))
                .intentName(GET_ARRIVALS_INTENT)
                .userId(USER_ID)
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