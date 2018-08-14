package com.merricklabs.portlandbus.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PortlandBusIntents {
    public static final String GET_ARRIVALS_INTENT = "GetArrivalsIntent";
    public static final String MY_STOP_INTENT = "MyStopIntent";
    public static final String SAVE_STOP_INTENT = "SaveStopIntent";
}
