package airscanner;

import java.sql.Timestamp;

/**
 * Created by Michał on 2016-12-26.
 */
public class FlightInfo
{
    public FlightInfo(int id, String source, String destination, String airlines, String duration, Timestamp arrivalTime, Timestamp departureTime)
    {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.airlines = airlines;
        this.duration = duration;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public int id;
    public String source;
    public String destination;
    public String airlines;
    public String duration;
    public Timestamp arrivalTime;
    public Timestamp departureTime;
}
