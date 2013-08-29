/**
 * Basic Drawing
 * by Andres Colubri. 
 * 
 * This program illustrates how to use the graphic tablet with the protablet library.
 */
 
import codeanticode.protablet.*;

Tablet tablet;

void setup() {
  size(640, 480);
 
  tablet = new Tablet(this); 
  
  background(0);
  stroke(255);  
}

void draw() {
  if (mousePressed) {
    strokeWeight(30 * tablet.getPressure());
    
    // Aside from tablet.getPressure(), which should be available on all pens, pen may support...
    //tablet.getTiltX(), tablet.getTiltY() MOST PENS
    //tablet.getSidePressure() - AIRBRUSH PEN
    //tablet.getRotation() - ART or PAINTING PEN    
    
    // The tablet getPen methods can be used to retrieve the pen current and saved position
    // (requires calling tablet.saveState() at the end of draw())...
    //line(tablet.getSavedPenX(), tablet.getSavedPenY(), tablet.getPenX(), tablet.getPenY());
    
    // ...but it is equivalent to use built-in Processing's mouse variables.
    line(pmouseX, pmouseY, mouseX, mouseY);
  }
  println("TiltX: " + tablet.getTiltX() + " " + "TiltY: " + tablet.getTiltY());
  
  // The current values (pressure, tilt, etc.) can be saved using the saveState() method
  // and latter retrieved with getSavedxxx() methods:
  //tablet.saveState();
  //tablet.getSavedPressure();
}
