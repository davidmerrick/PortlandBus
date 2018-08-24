package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.google.common.collect.ImmutableMap;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.PortlandBusIntegrationTestBase;
import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import com.merricklabs.portlandbus.mocks.MockMyStopStorage;
import com.merricklabs.portlandbus.mocks.MockTrimetClient;
import com.merricklabs.portlandbus.testutil.HandlerInputBuilder;
import java.util.List;
import java.util.Optional;
import org.testng.annotations.Test;

import static com.merricklabs.portlandbus.constants.PortlandBusIntents.MY_STOP_INTENT;
import static com.merricklabs.portlandbus.testutil.TestConstants.INTEGRATION_GROUP;
import static com.merricklabs.portlandbus.testutil.TestData.STOP_ID;
import static com.merricklabs.portlandbus.testutil.TestData.USER_ID;
import static org.testng.Assert.assertTrue;

public class MyStopIntentHandlerTest extends PortlandBusIntegrationTestBase {

    @Test(groups = INTEGRATION_GROUP)
    public void userHasStopSaved() {
        // Initialize mock trimet client
        List<Arrival> dummyArrivals = MockTrimetClient.getDummyArrivals(STOP_ID, 5);
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
        mockTrimetClient.setArrivals(dummyArrivals);

        // Initialize stop storage
        MockMyStopStorage mockMyStopStorage = injector.getInstance(MockMyStopStorage.class);
        mockMyStopStorage.saveStop(USER_ID, STOP_ID);

        HandlerInput input = getInput(USER_ID);
        MyStopIntentHandler myStopIntentHandler = injector.getInstance(MyStopIntentHandler.class);
        Optional<Response> responseOptional = myStopIntentHandler.handle(input);
        assertTrue(responseOptional.isPresent());

        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("Next arrivals at stop"));
        assertTrue(speechText.contains(String.valueOf(STOP_ID)));
        assertTrue(speechText.contains(", and bus"));
    }

    @Test(groups = INTEGRATION_GROUP)
    public void userDoesNotHaveStopSaved() {
        // Initialize mock trimet client
        List<Arrival> dummyArrivals = MockTrimetClient.getDummyArrivals(STOP_ID, 5);
        MockTrimetClient mockTrimetClient = injector.getInstance(MockTrimetClient.class);
        mockTrimetClient.setArrivals(dummyArrivals);

        HandlerInput input = getInput(USER_ID);
        MyStopIntentHandler myStopIntentHandler = injector.getInstance(MyStopIntentHandler.class);
        Optional<Response> responseOptional = myStopIntentHandler.handle(input);
        assertTrue(responseOptional.isPresent());

        String speechText = responseOptional.get().getOutputSpeech().toString();
        assertTrue(speechText.contains("You haven't saved a stop yet. Please save a stop first."));
    }

    private HandlerInput getInput(String userId) {
        PortlandBusConfig.Alexa alexaConfig = injector.getInstance(PortlandBusConfig.class).getAlexa();
        return HandlerInputBuilder.builder()
                .config(alexaConfig)
                .slots(ImmutableMap.of())
                .intentName(MY_STOP_INTENT)
                .userId(userId)
                .build()
                .getHandlerInput();
    }
}
