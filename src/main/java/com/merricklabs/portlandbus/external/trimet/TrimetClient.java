package com.merricklabs.portlandbus.external.trimet;

import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import java.util.List;

public interface TrimetClient {

    List<Arrival> getArrivalsForStop(int stopId);
}
