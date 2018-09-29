package com.merricklabs.portlandbus.external.trimet.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class ArrivalResults @JsonCreator
constructor(@param:JsonProperty("resultSet") var resultSet: ResultSet)
