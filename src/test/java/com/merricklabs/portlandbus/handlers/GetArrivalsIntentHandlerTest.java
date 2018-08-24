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
import static com.merricklabs.portlandbus.testutil.TestData.USER_ID;
import static org.testng.Assert.assertTrue;

@Slf4j
public class GetArrivalsIntentHandlerTest extends PortlandBusIntegrationTestBase {

    @Test(groups = INTEGRATION_GROUP)
    public void getNextArrivals(){
        final int stopId = 10791;

        // Initialize mock trimet client
        List<Arrival> dummyArrivals = MockTrimetClient.getDummyArrivals(stopId, 5);
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
        mockTrimetClient.setArrivals(dummyArrivals);

        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        HandlerInput input = HandlerInputBuilder.builder()
                .config(alexaConfig)
                .slots(buildSlots(String.valueOf(stopId), alexaConfig))
                .intentName(GET_ARRIVALS_INTENT)
                .userId(USER_ID)
                .build()
                .getHandlerInput();
        GetArrivalsIntentHandler handler = injector.getInstance(GetArrivalsIntentHandler.class);
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());
        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Next arrivals at stop"));
        assertTrue(speechText.contains(String.valueOf(stopId)));
        assertTrue(speechText.contains(", and bus"));
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