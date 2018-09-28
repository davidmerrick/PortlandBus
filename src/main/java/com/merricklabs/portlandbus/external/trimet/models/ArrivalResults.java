package com.merricklabs.portlandbus.external.trimet.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrivalResults {

    public ResultSet resultSet;

    @JsonCreator
    public ArrivalResults(@JsonProperty("resultSet") ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
