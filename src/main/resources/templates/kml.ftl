<?xml version="1.0" standalone="yes"?>
<kml xmlns="http://earth.google.com/kml/2.2">
    <Document>
        <Folder id="Track">
<#setting number_format="##0.000000">
<#setting locale="en_US">
<#list igcfile.getbRecords() as brecord>
    <#if lastbrecord??>
        <#assign climbrate=igcreader.calculateClimbRate(lastbrecord,brecord)>
            <Placemark>
                <LineString>
                    <altitudeMode>absolute</altitudeMode>
                    <coordinates>${lastbrecord.getLon()},${lastbrecord.getLat()},${lastbrecord.getAltitudePress() + igcfile.getAltitudePressureCompensation()} ${brecord.getLon()},${brecord.getLat()},${brecord.getAltitudePress() + igcfile.getAltitudePressureCompensation()}</coordinates>
                    <tessellate>0</tessellate>
                </LineString>
                <Style>
                    <LineStyle>
                        <color>${igcreader.getColorByClimbRate(climbrate)}</color>
                        <width>4</width>
                    </LineStyle>
                </Style>
            </Placemark>
    </#if>
    <#assign lastbrecord = brecord>
</#list>
            <name>Flight Track ${igcfile.getDate()}</name>
            <visibility>1</visibility>
        </Folder>
        <Snippet>Created by igc2kml (c) 2018 Stefan Märkle</Snippet>
        <name>${filename}</name>
        <description>${filedescription}</description>
        <visibility>1</visibility>
    </Document>
</kml>
