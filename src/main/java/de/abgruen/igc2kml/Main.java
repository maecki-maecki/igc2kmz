package de.abgruen.igc2kml;

import freemarker.template.TemplateException;
import java.io.IOException;

public class Main {

    public static void main(String [ ] args) throws IOException, TemplateException {
        IGCReader reader = new IGCReader(args[0],args[1]);
        reader.parse();
        reader.convert();
    }
}
