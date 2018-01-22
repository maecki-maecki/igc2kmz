<?xml version="1.0" standalone="yes"?>
<#setting number_format="##0.000000">
<#setting locale="en_US">
<kml xmlns="http://earth.google.com/kml/2.2">
    <Document>
        <Folder id="TakeoffAndLanding">
            <name>Takeoff / Landing Points</name>
<#assign takeoff=igcfile.getTakeOff()>
<#assign landing=igcfile.getLanding()>
            <Placemark>
                <name>Takeoff ${igcfile.getPilot()!"Unknown"} ${igcfile.getDate()} - ${takeoff.getTime()}</name>
                <description>
                    Pilot: ${igcfile.getPilot()!"Unknown"}<br/>
                    Glider: ${igcfile.getGliderType()!"Unknown"}<br/>
                    Alt.: ${takeoff.getAltitudeGps()}<br/>
                    Date: ${igcfile.getDate()}<br/>
                    Time: ${takeoff.getTime()}<br/>
                    Max Alt.:${igcfile.getMaxAltitude()}<br/>
                    Min Alt.:${igcfile.getMinAltitude()}<br/>
                </description>
                <Point>
                    <altitudeMode>absolute</altitudeMode>
                    <coordinates>${takeoff.getLon()},${takeoff.getLat()},${takeoff.getAltitudeGps()}</coordinates>
                </Point>
                <Style>
                    <IconStyle>
                        <Icon>
                            <href>airplane-takeoff-64.png</href>
                        </Icon>
                    </IconStyle>
                </Style>
            </Placemark>
            <Placemark>
                <name>Landing ${igcfile.getPilot()!"Unknown"} ${igcfile.getDate()} - ${landing.getTime()}</name>
                <description>
                    Pilot: ${igcfile.getPilot()!"Unknown"}<br/>
                    Glider: ${igcfile.getGliderType()!"Unknown"}<br/>
                    Alt.: ${landing.getAltitudeGps()}<br/>
                    Date: ${igcfile.getDate()}<br/>
                    Time: ${landing.getTime()}<br/>
                </description>
                <Point>
                    <altitudeMode>absolute</altitudeMode>
                    <coordinates>${landing.getLon()},${landing.getLat()},${landing.getAltitudeGps()}</coordinates>
                </Point>
                <Style>
                    <IconStyle>
                        <Icon>
                            <href>airplane-landing-64.png</href>
                        </Icon>
                    </IconStyle>
                </Style>
            </Placemark>
        </Folder>
        <Folder id="TrackBaro">
<#list igcfile.getbRecords() as brecord>
    <#if lastbrecord??>
        <#assign climbrate=igcreader.calculateClimbRate(lastbrecord,brecord)>
            <Placemark>
                <LineString>
                    <altitudeMode>absolute</altitudeMode>
                    <coordinates>${lastbrecord.getLon()},${lastbrecord.getLat()},${lastbrecord.getAltitudePress() + igcfile.getAltitudePressureCompensation(brecord_index)} ${brecord.getLon()},${brecord.getLat()},${brecord.getAltitudePress() + igcfile.getAltitudePressureCompensation(brecord_index)}</coordinates>
                    <tessellate>0</tessellate>
                </LineString>
                <Style>
                    <LineStyle>
                        <color>${igcreader.getColorByClimbRate(climbrate)}</color>
                        <width>4</width>
                    </LineStyle>
                </Style>
                <description>
                    Time: ${brecord.getTime()}<br/>
                    Alt.:${brecord.getAltitudePress() + igcfile.getAltitudePressureCompensation(brecord_index)}<br/>
                    Climb: ${climbrate}
                </description>
            </Placemark>
    </#if>
    <#assign lastbrecord = brecord>
</#list>
            <name>Baro Flight Track ${igcfile.getDate()}</name>
            <visibility>1</visibility>
        </Folder>
        <Snippet>Created by igc2kml (c) 2018 Stefan Märkle</Snippet>
        <name>${filename}</name>
        <description>${filedescription}</description>
        <visibility>1</visibility>
    </Document>
</kml>
