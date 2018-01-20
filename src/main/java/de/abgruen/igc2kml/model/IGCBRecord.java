package de.abgruen.igc2kml.model;

import java.util.Date;

public class IGCBRecord {
    private static final int TIME_START_INDEX = 1;
    private static final int TIME_END_INDEX = 7;
    private static final int LAT_START_INDEX = 7;
    private static final int LAT_END_INDEX = 15;
    private static final int LON_START_INDEX = 15;
    private static final int LON_END_INDEX = 24;
    private static final int FIX_VALIDITY_START_INDEX = 24;
    private static final int ALTITUDE_PRESS_START_INDEX = 25;
    private static final int ALTITUDE_PRESS_END_INDEX = 30;
    private static final int ALTITUDE_GPS_START_INDEX = 30;
    private static final int ALTITUDE_GPS_END_INDEX = 35;

    private String time;
    private Date timed;
    private String lat;
    private Double latd;
    private Double lond;
    private String lon;
    private int altitudePress;
    private int altitudeGps;

    public IGCBRecord(String rawRecord) {
        time = rawRecord.substring(TIME_START_INDEX, TIME_END_INDEX);
        timed = parseTime(time);
        lat = rawRecord.substring(LAT_START_INDEX, LAT_END_INDEX);
        latd = parseLat(lat);
        lon = rawRecord.substring(LON_START_INDEX, LON_END_INDEX);
        lond = parseLon(lon);
        altitudePress = Integer.valueOf(rawRecord.substring(ALTITUDE_PRESS_START_INDEX, ALTITUDE_PRESS_END_INDEX));
        altitudeGps = Integer.valueOf(rawRecord.substring(ALTITUDE_GPS_START_INDEX, ALTITUDE_GPS_END_INDEX));
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getLat() {
        return latd;
    }

    public Double getLon() {
        return lond;
    }


    public int getAltitudePress() {
        return altitudePress;
    }

    public int getAltitudeGps() {
        return altitudeGps;
    }

    public int getAltitude() {
        if (getAltitudeGps() == 0) {
            return getAltitudePress();
        }
        return getAltitudeGps();
    }


    protected Double parseLat(String raw){
        String deg = raw.substring(0,2);
        String min = raw.substring(2,4);
        String mindec = raw.substring(4,7);
        Double result = Double.valueOf(deg);
        result *= raw.toUpperCase().endsWith("N")?1D:-1D;
        Double minutes = Double.valueOf(min)+ Double.valueOf(mindec)/1000;
        result += minutes/60D;
        return result;
    }

    protected Double parseLon(String raw){
        String deg = raw.substring(0,3);
        String min = raw.substring(3,5);
        String mindec = raw.substring(5,8);
        Double result = Double.valueOf(deg);
        result *= raw.toUpperCase().endsWith("E")?1D:-1D;
        Double minutes = Double.valueOf(min)+ Double.valueOf(mindec)/1000;
        result += minutes/60D;
        return result;
    }

    protected Date parseTime(String raw){
        String h = raw.substring(0,2);
        String m = raw.substring(2,4);
        String s = raw.substring(4,6);
        return new Date(); // TODO Date is in another header line HFDTE ...
    }

}
