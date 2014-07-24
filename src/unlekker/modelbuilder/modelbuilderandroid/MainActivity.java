package unlekker.modelbuilder.modelbuilderandroid;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import unlekker.modelbuilder.*; 
import unlekker.modelbuilder.filter.*; 
import unlekker.util.*; 

import unlekker.util.*; 
import controlP5.*; 
import unlekker.modelbuilder.*; 
import unlekker.modelbuilder.filter.*; 
import ec.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MainActivity extends PApplet {

/**
 * DeformModel02.pde - Marius Watz, 2012
 * http://workshop.evolutionzone.com
 * 
 * Demonstrates use of unlekker.modelbuilder.filter.UTransformDeform,
 * allowing various deformations of mesh objects.
 * 
 */



//import processing.opengl.*;





UGeometry geo,loadedModel;
float amount=0.5f;
int u=8, v=16;

public void setup() {
 

  initGUI();
  resetForm();
}

public void draw() {
  background(0);

  pushMatrix();
  lights();
  nav.doTransforms();

  fill(255);
  stroke(50, 50);
  geo.draw(this);

  noFill();
  stroke(255, 100);
  for (float i=0; i<11; i++) {
    for (float j=0; j<11; j++) {
      line(i*50-250, 0, -250, i*50-250, 0, 250);
      line(-250, 0, i*50-250, 250, 0, i*50-250);
    }
  }
  popMatrix();



  hint(DISABLE_DEPTH_TEST);
  gui.draw();
  hint(ENABLE_DEPTH_TEST);
}

USimpleGUI gui;
UNav3D nav;

public void initGUI() {
  nav=new UNav3D(this);
  nav.setTranslation(width/2, height/2+200, 0);
  nav.setRotation(PI, 0, 0);

  gui=new USimpleGUI(this);
  gui.addSlider("amount", amount, -1, 1);
  gui.addSlider("u", u, 2, 30);
  gui.addSlider("v", v, 2, 30);
  gui.newRow();
  gui.addButton("rotX").addButton("rotY").addButton("rotZ");
  gui.addButton("bend").addButton("taper").addButton("twist");
  gui.newRow();
  gui.addButton("resetForm");
  gui.addButton("loadSTL");
  gui.addButton("saveSTL");
  gui.setLayout(false);
}


// demonstrates how to load a STL file using a file chooser dialog
public void loadSTL() {
  String filename=UIO.getFilenameChooserDialog(this, sketchPath);
  if (filename.endsWith("stl")) {
    loadedModel=UGeometry.readSTL(this, filename);
    loadedModel.setDimensions(height);
    loadedModel.center().calcBounds();
    loadedModel.translate(0, -geo.bb.min.y, 0);			
    
    resetForm();
  }
}

public void saveSTL() {
  String nameFormat=this.getClass().getSimpleName();
  nameFormat=nameFormat+" ####.png";

  String filename=UIO.getIncrementalFilename(nameFormat, sketchPath);
  saveFrame(filename);

  println("Saved '"+filename+"'");
  geo.writeSTL(this, UIO.noExt(filename)+".stl");
}

public void resetForm() {
  if (loadedModel!=null) geo=new UGeometry(loadedModel);
  else geo=UPrimitive.cylinderGrid(60, 200, u, v, true);
  geo.calcBounds();
  geo.translate(0, -geo.bb.min.y, 0);
}

public void bend() {
  new UTransformDeform().bend(amount*radians(90)).transform(geo);
}

public void twist() {
  new UTransformDeform().twist(amount*radians(90)).transform(geo);
}

public void taper() {
  new UTransformDeform().taper(amount).transform(geo);
}

public void rotX() {
  geo.rotateX(amount*HALF_PI);
}

public void rotY() {
  geo.rotateY(amount*HALF_PI);
}

public void rotZ() {
  geo.rotateZ(amount*HALF_PI);
}


  public int sketchWidth() { return 800; }
  public int sketchHeight() { return 600; }
  public String sketchRenderer() { return OPENGL; }
}

