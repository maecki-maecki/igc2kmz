package de.abgruen.igc2kml;

import de.abgruen.igc2kml.model.IGCBRecord;
import de.abgruen.igc2kml.model.IGCFile;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class IGCReader {
    String inputFile;
    String outputFile;
    public IGCReader(String inputFile, String outputFile){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void parse() throws IOException, TemplateException {

        IGCFile igcFile = new IGCFile(inputFile);
        System.out.println(String.format("File parsed. Pilot %s on Wing %s at %s", igcFile.getPilot(), igcFile.getGliderType(), igcFile.getDate()));
        System.out.println(String.format("Takeoff time: %s, Landing time: %s, max altitude: %d, min altitude %d.", igcFile.getTakeOffTime(), igcFile.getLandingTime(), igcFile.getMaxAltitude(), igcFile.getMinAltitude()));
//        generateTakeOffAndLandingMark(igcFile,document);
        //       generateTrack(igcFile,document);
        Configuration cfg = new Configuration(new Version("2.3.27-incubating"));
        cfg.setClassForTemplateLoading(this.getClass(),"/templates/");
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("kml.ftl");

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("igcfile", igcFile);
        templateData.put("filename",inputFile);
        templateData.put("filedescription",String.format("Pilot %s on Wing %s at %s", igcFile.getPilot(), igcFile.getGliderType(), igcFile.getDate()));
        templateData.put("igcreader", this);
        try (StringWriter out = new StringWriter()) {

            template.process(templateData, out);
            out.flush();
            FileWriter fw = new FileWriter(outputFile);
            fw.write(out.toString());
            fw.close();
        }
    }

    public String getColorByClimbRate(Double climbRate) {
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

    public Double calculateClimbRate(IGCBRecord first, IGCBRecord second){
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
