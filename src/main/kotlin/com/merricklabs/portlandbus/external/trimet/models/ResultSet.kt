package com.merricklabs.portlandbus.external.trimet.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class ResultSet(@param:JsonProperty("arrival") var arrivals: List<Arrival>)
