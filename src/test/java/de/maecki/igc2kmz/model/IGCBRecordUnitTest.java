package de.maecki.igc2kmz.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IGCBRecordUnitTest {

    @Test
    public void testNormalRecord(){
        IGCBRecord record = new IGCBRecord("B1151564752946N00656593EA0042900469");
        assertEquals("11:51:56",record.getTime());
        assertEquals(47.882433D,record.getLat(),0.00001D);
        assertEquals( 6.943217D,record.getLon(),0.00001D);
        assertEquals(429, record.getAltitudePress());
        assertEquals(469, record.getAltitudeGps());
    }

    @Test
    public void testSouthernHemisphere(){
        IGCBRecord record = new IGCBRecord("B1151564752946S00656593EA0042900469");
        assertEquals(-47.882433D,record.getLat(),0.00001D);
        assertEquals( 6.943217D,record.getLon(),0.00001D);
    }

    @Test
    public void testWesternLongitude(){
        IGCBRecord record = new IGCBRecord("B1151564752946N00656593WA0042900469");
        assertEquals(47.882433D,record.getLat(),0.00001D);
        assertEquals( -6.943217D,record.getLon(),0.00001D);
    }
}
