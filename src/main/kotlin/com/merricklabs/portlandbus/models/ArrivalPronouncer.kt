package com.merricklabs.portlandbus.models

import com.merricklabs.portlandbus.external.trimet.models.Arrival
import java.time.LocalDateTime

/**
 * Presenter class for arrivals.
 * Separates arrival data from presentation data.
 */
class ArrivalPronouncer(private val arrival: Arrival) {
    private val minutesRemaining by lazy { arrival.getMinutesRemaining(LocalDateTime.now()) }

    private fun getMinutePronunciation(duration: Long): String {
        return if (duration == 1L) {
            "minute"
        } else "minutes"
    }

    /**
     * Pronunciation when as part of a list of arrivals.
     * @param now
     * @return
     */
    fun pronounceForMultipleArrival(): String {
        return "bus ${arrival.busId} in $minutesRemaining ${getMinutePronunciation(minutesRemaining)}"
    }

    fun showNextArrival(): String {
        val minutesRemaining = minutesRemaining
        return "Bus ${arrival.busId}: $minutesRemaining min"
    }
}
