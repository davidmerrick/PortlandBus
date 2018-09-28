package com.merricklabs.portlandbus.models

import com.merricklabs.portlandbus.external.trimet.models.Arrival
import java.time.LocalDateTime

/**
 * Presenter class for arrivals.
 * Separates arrival data from presentation data.
 */
class ArrivalPronouncer(private val arrival: Arrival) {
    private var minutesRemainingCache: Long? = null

    private fun getMinutePronunciation(duration: Long): String {
        return if (duration == 1L) {
            "minute"
        } else "minutes"

    }

    /**
     * Cache the result of minutes remaining calculation, so display is consistent between the show results
     * and the spoken ones.
     *
     * @param now
     * @return
     */
    private fun getMinutesRemainingCache(now: LocalDateTime): Long {
        if (minutesRemainingCache == null) {
            minutesRemainingCache = arrival.getMinutesRemaining(now)
        }

        return minutesRemainingCache!!
    }

    /**
     * Pronunciation when as part of a list of arrivals.
     * @param now
     * @return
     */
    fun pronounceForMultipleArrival(now: LocalDateTime): String {
        val minutesRemaining = getMinutesRemainingCache(now)
        return "bus " + arrival.busId + " in " + minutesRemaining + " " + getMinutePronunciation(minutesRemaining)
    }

    fun showNextArrival(now: LocalDateTime): String {
        val minutesRemaining = getMinutesRemainingCache(now)
        return "Bus " + arrival.busId + ": " + minutesRemaining + " min"
    }
}
