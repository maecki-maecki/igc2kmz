package de.maecki.igc2kmz.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class IGCFileUnitTest {

    IGCFile igcFile = null;

    @BeforeEach
    public void testLoadFile() {
        if (igcFile==null) {
            try {
                igcFile = new IGCFile("src/test/resources/demo.igc");
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }
        assertNotNull(igcFile);
    }

    @Test
    public void testBRecordParsing() {
        // B1143334753624N00656065EA0099101016
        IGCBRecord first = igcFile.getTakeOff();
        assertEquals(47.893733333D, first.getLat().doubleValue(),0.000001D);
        assertEquals(6.934416667D, first.getLon().doubleValue(), 0.000001D);
        assertEquals(991, first.getAltitudePress());
        assertEquals(1016, first.getAltitudeGps());
        assertEquals("11:43:33", first.getTime());
    }

    @Test
    public void testPilotAndGliderData() {
        // HFPLTPILOTINCHARGE:Stefan Märkle
        assertEquals("Stefan Märkle", igcFile.getPilot());
        // HFGTYGLIDERTYPE:Gin Bolero 5
        assertEquals("Gin Bolero 5", igcFile.getGliderType());
        // HFGIDGLIDERID:
        assertEquals("", igcFile.getGliderID());
        // HFDTE140118
        assertEquals("14.01.18", igcFile.getDate());
    }

    @Test
    public void testMinMaxAltitude() {
        assertEquals(453, igcFile.getMinAltitude()); // interpolated value at first landed datza point
        assertEquals(1016, igcFile.getMaxAltitude());
    }

    @Test
    public void testAltitudePressureCompensation() {
        assertEquals(25, igcFile.getAltitudePressureCompensation(0)); // 991, 1016
        assertEquals(40, igcFile.getAltitudePressureCompensation(igcFile.getbRecords().size()-1)); // 418, 459 and round
        assertEquals(33, igcFile.getAltitudePressureCompensation(igcFile.getbRecords().size()/2)); // avg(25,41)
    }


}