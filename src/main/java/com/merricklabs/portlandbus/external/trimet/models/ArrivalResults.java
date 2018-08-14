package com.merricklabs.portlandbus.external.trimet.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrivalResults {

    private ResultSet resultSet;

    @JsonCreator
    public ArrivalResults(@JsonProperty("resultSet") ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
