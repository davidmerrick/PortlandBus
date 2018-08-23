package com.merricklabs.portlandbus.mocks;

import com.google.common.collect.ImmutableList;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import java.util.List;
import lombok.Setter;

public class MockTrimetClient implements TrimetClient {

    @Setter
    private List<Arrival> arrivals = ImmutableList.of();

    @Override
    public List<Arrival> getArrivalsForStop(int stopId) {
        return arrivals;
    }

    public void reset(){
        arrivals = ImmutableList.of();
    }
}
