package de.abgruen.igc2kml;

import java.io.File;
import java.io.IOException;

public class IGC2KMLApplication {

    public static void main(String [ ] args) throws IOException {
        IGCReader reader = new IGCReader(args[0],"src/main/resources/out.kml");
        reader.parse();
    }
}
