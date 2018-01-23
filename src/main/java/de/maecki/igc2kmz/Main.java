package de.maecki.igc2kmz;

import freemarker.template.TemplateException;
import java.io.IOException;

public class Main {

    public static void main(String [ ] args) throws IOException, TemplateException {
        // --version
        if (args[0].toLowerCase().equals("--version")){
            System.out.println( "igc2kml Version: " + Main.class.getPackage().getImplementationVersion() );
            System.out.println( "(c) Stefan Märkle");
            System.exit(0);
        }

        // --help
        if (args[0].toLowerCase().equals("--help")){
            System.out.println( "igc2kml Version:" + Main.class.getPackage().getImplementationVersion() );
            System.out.println( "(c) Stefan Märkle");
            System.out.println( "");
            System.out.println( "usage: java -jar igc2kml.jar [--version] [--help] INPUTFILENAME [OUTPUTFILENAME]");
            System.out.println( "");
            System.out.println( "--version     : show version and exit");
            System.out.println( "--help        : show this help and exit");
            System.out.println( "INPUTFILENAME : filename of a valid igc file to be processed");
            System.out.println( "OUTPUTFILENAME: filename of the kmz file to be generated. If omitted filename will be derived from input filename");
            System.exit(0);
        }

        String outputFilename;
        if (args.length<2){
            // just inputfilename
            outputFilename = generateOutputFilename(args[0]);
        } else {
            // inputfilename and outputfilename
            outputFilename=args[1];
        }
        IGCReader reader = new IGCReader(args[0],outputFilename);
        reader.parse();
        reader.convert();
    }

    private static String generateOutputFilename(String inputFilename){
        String body = inputFilename;
        int extensionStart = body.lastIndexOf(".");
        if (extensionStart>0) {
            body = body.substring(0, extensionStart);
        }
        return body+".kmz";
    }
}
