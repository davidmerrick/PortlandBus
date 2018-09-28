package com.merricklabs.portlandbus.external.trimet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSet {

    public List<Arrival> arrivals;

    public ResultSet(@JsonProperty("arrival") List<Arrival> arrival) {
        this.arrivals = arrival;
    }
}
