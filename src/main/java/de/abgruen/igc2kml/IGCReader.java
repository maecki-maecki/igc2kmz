package de.abgruen.igc2kml;

import de.abgruen.igc2kml.model.IGCBRecord;
import de.abgruen.igc2kml.model.IGCFile;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.*;
import java.net.URI;
import java.util.HashMap;

public class IGCReader {
    String inputFile;
    String outputFile;
    public IGCReader(String inputFile, String outputFile){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void parse() throws IOException {

        final Kml kml = new Kml();
        Document document = kml.createAndSetDocument();

       IGCFile igcFile = new IGCFile(inputFile);
        System.out.println(String.format("File parsed. Pilot %s on Wing %s at %s",igcFile.getPilot(),igcFile.getGliderType(),igcFile.getDate()));
        System.out.println(String.format("Takeoff time: %s, Landing time: %s, max altitude: %d, min altitude %d.",igcFile.getTakeOffTime(),igcFile.getLandingTime(),igcFile.getMaxAltitude(),igcFile.getMinAltitude()));
        generateTakeOffAndLandingMark(igcFile,document);
        generateTrack(igcFile,document);
        kml.marshal(new File(outputFile));
    }


    protected void generateTakeOffAndLandingMark(IGCFile igcFile, Document document) {
        Folder folder = document.createAndAddFolder();
        String description = "%s on %s (%s). Takeoff time: %s, landing time: %s, max altitude: %d, min altitude %d";
        folder.withName(String.format(description, igcFile.getPilot(),igcFile.getGliderType(),igcFile.getDate(),igcFile.getTakeOffTime(),igcFile.getLandingTime(),igcFile.getMaxAltitude(),igcFile.getMinAltitude()));
        IGCBRecord igcbRecordTakeOff=igcFile.getbRecords().get(0);
        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(String.format("%s on %s (%s). Takeoff %s",igcFile.getPilot(),igcFile.getGliderType(),igcFile.getDate(),igcFile.getTakeOffTime()));
        placemark.createAndSetPoint().addToCoordinates(igcbRecordTakeOff.getLon(),igcbRecordTakeOff.getLat(),new Double(igcbRecordTakeOff.getAltitudeGps()));
        IGCBRecord igcbRecordLanding=igcFile.getbRecords().get(igcFile.getbRecords().size()-1);
        Placemark placemark1 = folder.createAndAddPlacemark();
        placemark1.withName(String.format("%s on %s (%s). Landing %s",igcFile.getPilot(),igcFile.getGliderType(),igcFile.getDate(),igcFile.getLandingTime()));
        placemark1.createAndSetPoint().addToCoordinates(igcbRecordLanding.getLon(),igcbRecordLanding.getLat(),new Double(igcbRecordLanding.getAltitudeGps()));
    }

    protected void generateTrack(IGCFile igcFile, Document document){
        IGCBRecord lastBRecord = null;
        //lineString.withExtrude(true);

        Folder folder = document.createAndAddFolder();
        folder.withName("Track");
        for (IGCBRecord bRecord: igcFile.getbRecords()){
            Placemark placemark=folder.createAndAddPlacemark();

            Double climbRate = 0d;
            if (lastBRecord!=null){
                climbRate = calculateClimbRate(lastBRecord,bRecord);
            }
            LineString lineString = placemark.createAndSetLineString();
            if (lastBRecord!=null) {
                lineString.addToCoordinates(lastBRecord.getLon(), lastBRecord.getLat(), new Double(lastBRecord.getAltitudePress() + igcFile.getAltitudePressureCompensation())).withAltitudeMode(AltitudeMode.ABSOLUTE);
            }
            lineString.addToCoordinates(bRecord.getLon(), bRecord.getLat(), new Double(bRecord.getAltitudePress()+igcFile.getAltitudePressureCompensation())).withAltitudeMode(AltitudeMode.ABSOLUTE);

            LineStyle lineStyle = placemark.createAndAddStyle().createAndSetLineStyle();
            lineStyle.withColor(getColorByClimbRate(climbRate)).withWidth(3.0D);
            lastBRecord = bRecord;
        }
    }

    protected String getColorByClimbRate(Double climbRate) {
        if ( -5D > climbRate){
            return "#DD00DDFF";
        }
        if ( -0.8D > climbRate && climbRate >= -5D){
            return "#AA8080FF";
        }
        if ( 0D > climbRate && climbRate >= -0.8D){
            return "#22AAAAFF";
        }
        if ( 1.5D > climbRate && climbRate >= 0F){
            return "#008020FF";
        }
        if ( climbRate >= 1.5D){
            return "#00A000FF";
        }
        return "#EEEEEEFF";
    }

    protected Double calculateClimbRate(IGCBRecord first, IGCBRecord second){
        Double timeInSeconds = secondsSinceMidnight(second.getTime())-secondsSinceMidnight(first.getTime());
        return (second.getAltitudePress()-first.getAltitudePress())/timeInSeconds;
    }

    protected Double secondsSinceMidnight(String igcstring){
        String hour = igcstring.substring(0,2);
        String min = igcstring.substring(2,4);
        String sec = igcstring.substring(4,6);
        return Double.valueOf(sec)+60D*Double.valueOf(min)+60D*60D*Double.valueOf(hour);
    }

}
