package de.abgruen.igc2kml.model;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IGCFile {
    private String pilot;
    private String gliderType;
    private String gliderID;
    private String date;
    private int maxAltitude=-100000;
    private int minAltitude=100000;
    private String takeOffTime;
    private String landingTime;
    private List<IGCBRecord> bRecords = new ArrayList<IGCBRecord>();
    private int altitudePressureCompensationTakeoff=0;
    private int altitudePressureCompensationLanding=0;

    public int getAltitudePressureCompensation(int i) {
        Double flightProgress = 1D*i/getbRecords().size();
        return new Double(altitudePressureCompensationTakeoff+flightProgress*(altitudePressureCompensationLanding - altitudePressureCompensationTakeoff)).intValue();
    }

    public String getPilot() {
        return pilot;
    }

    public String getGliderType() {
        return gliderType;
    }

    public String getGliderID() {
        return gliderID;
    }

    public String getDate() {

        return date.substring(0,2)+"."+date.substring(2,4)+"."+date.substring(4,6);
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public int getMinAltitude() {
        return minAltitude;
    }

    public String getTakeOffTime() {
        return takeOffTime;
    }

    public String getLandingTime() {
        return landingTime;
    }

    public List<IGCBRecord> getbRecords() {
        return bRecords;
    }

    public IGCFile(String filename) throws IOException{
        super();
        this.parse(filename);
    }

    public void parse(String fileName) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
        calculateStatistics();
    }

    protected void parseLine(String line){
        if (line.startsWith("B")){
            IGCBRecord bRecord = new IGCBRecord(line);
            bRecords.add(bRecord);
        }
        if (line.startsWith("HFDTE")) {
            date = line.substring(5);
        }
        if (line.startsWith("HFPLTPILOTINCHARGE:")) {
            pilot = line.substring(19);
        }
        if (line.startsWith("HFGTYGLIDERTYPE:")) {
            gliderType = line.substring(16);
        }
        if (line.startsWith("HFGIDGLIDERID:")) {
            gliderID = line.substring(14);
        }
   }

   protected void calculateStatistics(){
        takeOffTime = getTakeOff().getTime();
        altitudePressureCompensationTakeoff=getTakeOff().getAltitudeGps()-getTakeOff().getAltitudePress();
        altitudePressureCompensationLanding=getLanding().getAltitudeGps()-getLanding().getAltitudePress();
        landingTime = getLanding().getTime();

        List<Integer> altidudeDifferences = new ArrayList<Integer>();
        int i=0;
        for (IGCBRecord rec:bRecords){
            if (rec.getAltitudePress()+getAltitudePressureCompensation(i)>maxAltitude){
                maxAltitude=rec.getAltitudePress()+getAltitudePressureCompensation(i);
            }
            if (rec.getAltitudePress()+getAltitudePressureCompensation(i)<minAltitude){
                minAltitude=rec.getAltitudePress()+getAltitudePressureCompensation(i);
            }
            i++;
        }
    }

   public IGCBRecord getTakeOff(){
        return bRecords.get(0);
   }

    public IGCBRecord getLanding(){
        return bRecords.get(bRecords.size()-1);
    }

}
