package com.merricklabs.portlandbus.models;

import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ArrivalListPronouncer {

    private List<ArrivalPronouncer> arrivalPronouncers;
    private int stopId;
    private static final LocalDateTime now = LocalDateTime.now();
    private static final int MAX_ARRIVAL_MINUTES = 30;

    public ArrivalListPronouncer(int stopId, List<Arrival> arrivals) {

        // Filter out arrivals that are in the past and below a time threshold
        this.arrivalPronouncers = arrivals.stream()
                .filter(a -> a.getTime().isAfter(now))
                .filter(a -> a.getMinutesRemaining(now) < MAX_ARRIVAL_MINUTES)
                .map(ArrivalPronouncer::new)
                .collect(toList());
        this.stopId = stopId;
    }

    public static String pronounceStop(int stopId){
        if(stopId <= 1000){
            return String.valueOf(stopId);
        } else {
            return "<say-as interpret-as=\"digits\">" + stopId + "</say-as>";
        }
    }

    private String pronounceStop(){
        return pronounceStop(stopId);
    }

    public String pronounceArrivals(){
        final LocalDateTime now = LocalDateTime.now();

        if (arrivalPronouncers.size() == 0){
            return "No arrivals are currently scheduled for stop " + pronounceStop() + " in the next " + MAX_ARRIVAL_MINUTES + " minutes.";
        }

        StringBuilder speechBuilder = new StringBuilder("Next arrivals at stop " + pronounceStop() + ": ");

        if (arrivalPronouncers.size() == 1){
            speechBuilder.append(arrivalPronouncers.get(0).pronounceForMultipleArrival(now));
        } else {
            arrivalPronouncers.subList(0, arrivalPronouncers.size() - 1)
                    .stream()
                    .forEach(a -> speechBuilder.append(a.pronounceForMultipleArrival(now) + ", "));

            speechBuilder.append("and " + arrivalPronouncers.get(arrivalPronouncers.size() - 1).pronounceForMultipleArrival(now) + ".");
        }
        return speechBuilder.toString();
    }

    /**
     * Format arrivals for display on Echo Show
     * @return
     */
    public String showArrivals(){
        StringBuilder textBuilder = new StringBuilder("Stop " + stopId + ": \n");

        if (arrivalPronouncers.size() == 0){
            textBuilder.append("No arrivals");
        } else {
            arrivalPronouncers
                    .stream()
                    .forEach(a -> textBuilder.append(a.showNextArrival(now) + "\n"));
        }
        return textBuilder.toString();
    }
}
