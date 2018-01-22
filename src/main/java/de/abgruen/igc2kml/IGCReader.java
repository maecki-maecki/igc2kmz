package de.abgruen.igc2kml;

import de.abgruen.igc2kml.model.IGCBRecord;
import de.abgruen.igc2kml.model.IGCFile;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class IGCReader {
    String inputFile;
    String outputFile;

    IGCFile igcFile = null;

    public IGCReader(String inputFile, String outputFile){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void parse() throws IOException {

        igcFile = new IGCFile(inputFile);
        System.out.println(String.format("File parsed. Pilot %s on Wing %s at %s", igcFile.getPilot(), igcFile.getGliderType(), igcFile.getDate()));
        System.out.println(String.format("Takeoff time: %s, Landing time: %s, max altitude: %d, min altitude %d.", igcFile.getTakeOffTime(), igcFile.getLandingTime(), igcFile.getMaxAltitude(), igcFile.getMinAltitude()));
    }

    public void convert() throws IOException, TemplateException {
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
            FileOutputStream dest = new
                    FileOutputStream(outputFile);
            ZipOutputStream zos = new
                    ZipOutputStream(new BufferedOutputStream(dest));
            zos.putNextEntry(new ZipEntry("data.kml"));
            zos.write(out.toString().getBytes("UTF-8"));
            zos.closeEntry();
            addFileToZip("images/airplane-takeoff-64.png","airplane-takeoff-64.png",zos);
            addFileToZip("images/airplane-landing-64.png","airplane-landing-64.png",zos);

            zos.close();
        }
    }

    private void addFileToZip(String sourceFilename, String destinationFileName, ZipOutputStream zos) throws IOException {
        ZipEntry zipEntry = new ZipEntry(destinationFileName);
        zos.putNextEntry(zipEntry);
        InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream(sourceFilename);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            zos.write(buf, 0, bytesRead);
        }
        zos.closeEntry();
    }

    public String getColorByClimbRate(Double climbRate) {
        Color result = new Color (0,0,0);
        if (climbRate<0){
            Double sink = climbRate<-3?0:-1*climbRate/3D;
            result = new Color (new Double(0+sink*200).intValue(), 0,0);
        }
        if (climbRate>=0){
            Double climb = climbRate>4?0:climbRate/4D;
            result = new Color(0,new Double(0+climb*200).intValue(),0);
        }
        return colorAsHexString(result);
    }

    public Double calculateClimbRate(IGCBRecord first, IGCBRecord second){
        Double timeInSeconds = second.secondsSinceMidnight()-first.secondsSinceMidnight();
        return (second.getAltitudePress()-first.getAltitudePress())/timeInSeconds;
    }
    protected String colorAsHexString(Color c){
        return String.format("#%02X%02X%02X%02X",c.getAlpha(), c.getBlue(), c.getGreen(), c.getRed());
    }

}
