package com.merricklabs.portlandbus.models;

import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import java.time.LocalDateTime;

/**
 * Presenter class for arrivals.
 * Separates arrival data from presentation data.
 */
public class ArrivalPronouncer {

    private Arrival arrival;
    private Long minutesRemainingCache;

    public ArrivalPronouncer(Arrival arrival) {
        this.arrival = arrival;
    }

    private static String getMinutePronunciation(long duration){
        if (duration == 1){
            return "minute";
        }

        return "minutes";
    }

    /**
     * Cache the result of minutes remaining calculation, so display is consistent between the show results
     * and the spoken ones.
     *
     * @param now
     * @return
     */
    private long getMinutesRemainingCache(LocalDateTime now) {
        if (minutesRemainingCache == null) {
            minutesRemainingCache = arrival.getMinutesRemaining(now);
        }

        return minutesRemainingCache;
    }

    /**
     * Pronunciation when as part of a list of arrivals.
     * @param now
     * @return
     */
    public String pronounceForMultipleArrival(LocalDateTime now) {
        long minutesRemaining = getMinutesRemainingCache(now);
        return "bus " + arrival.getBusId() + " in " + minutesRemaining + " " + getMinutePronunciation(minutesRemaining);
    }

    public String showNextArrival(LocalDateTime now) {
        long minutesRemaining = getMinutesRemainingCache(now);
        return "Bus " + arrival.getBusId() + ": " + minutesRemaining + " min";
    }
}
