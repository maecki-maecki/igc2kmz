# IGC2KMZ
(c) 2018 by Stefan Märkle <maecki@maecki.de>

## Introduction
I wanted to view the climb rates of my paragliding flights in google earth but found no software that was able to color-code this into a google earth file. So i wrote the software myself.

![Screenshot](screenshot.png)

## Build

'mvn clean package' builds a jar file that can be executed 

## Execute

java -jar igc2kmz.jar myinputfile.igc myoutputfile.kmz

This reads in myinputfile.igc in igc format and builds an output file myoutputfile.kmz in kmz format suitable for google earth.

It also can be called with just on filename. It then takes that filename, strips extension and adds kmz extension to use as output filename.

## Logic

- All times are UTC, nothing converted.
- All altitudes are in meters, nothing converted.

### Altitude logic
- Barometric altitude can differ significantly from Gps altitude due to weather changes (even during the flight, e.g. for long xc flights) 
- Barometric altitude has a finer resolution than Gps altitude.
- Thus altitude info is taken from barometric pressure but corrected with the help of the gps altitude.
- The solution in avionics is to either use a reference pressure ('QNH') or at least ust the real altitude from a map of the startpoint as reference.
- I do not have QNHs for my flights and I do not want to need an internet connection for map reference
- So the correction I do is a linear interpolation of the difference between GPS and barometric altitude at takeoff and at landing.

## Copyrights and Disclaimer
- This software is (c) by Stefan Märkle
- This software is provided under Apache License 2.0, see @LICENSE
- Starting and landing icons are (C) from Becris https://www.shareicon.net/search?c=becris under Creative Commons Attribution 3.0 license.
- The software is provided 'as-is'. I do not hold liable for anything you do with it.