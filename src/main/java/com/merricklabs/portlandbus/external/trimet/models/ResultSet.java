package com.merricklabs.portlandbus.external.trimet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSet {

    List<Arrival> arrivals;

    public ResultSet(@JsonProperty("arrival") List<Arrival> arrival) {
        this.arrivals = arrival;
    }
}
