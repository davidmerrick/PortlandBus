package com.merricklabs.portlandbus.external.trimet

import com.merricklabs.portlandbus.external.trimet.models.Arrival

interface TrimetClient {
    fun getArrivalsForStop(stopId: Int): List<Arrival>
}
