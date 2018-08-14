/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.merricklabs.portlandbus.PortlandBusConfig;
import java.util.Optional;
import javax.inject.Inject;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    private final PortlandBusConfig config;

    @Inject
    public LaunchRequestHandler(PortlandBusConfig config) {
        this.config = config;
    }


    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String repromptText = "Which stop would you like information about?";
        final String INVOCATION_NAME = config.getAlexa().getInvocationName();
        String speechText = new StringBuilder()
                .append("Welcome to " + INVOCATION_NAME + ". ")
                .append("I can retrieve arrival times for bus stops in Portland, Oregon. ")
                .append(repromptText)
                .toString();
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(INVOCATION_NAME, speechText)
                .withReprompt(repromptText)
                .build();
    }

}
