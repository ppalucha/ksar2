# ksar2
Fork of ksar - a sar grapher

Ksar2 is a standalone Java application which allows to create graphs of data generated 
by Unix/Linux sa/sar commands.

This is fork of ksar project hosted originally at sourceforce.net. Since original project 
seams to be abandoned, this fork is created to maintain and develop the application.

If you want just to run the application, download JAR file from releases page and run 
with `java -jar <JAR FILE NAME>`. To compile, you need to have Maven installed, in source directory run `mvn install`. IDE project configuration is included for NetBeans. You need to use JDK >=6 and <= 8.

# News
v0.0.5
 - Build system changed to Maven, dependent libraries are no longer included in source.

v0.0.4
 - File open fixes.

v0.0.3
 - New command line handling.
 - PDF export fixes.

v0.0.2
 - Auto detecting Linux date/time format.
 - Added missing date column in CSV export.

v0.0.1 
 - Parsing RedHat 7.x sar files.


