

/*
 * This interface declares all methods which must be overriden by classes of implementation. The
 * methods are those which decide what happens when a user utilizes the mouse on different objects
 * in the application
 * 
 * @author uw cs staff
 */

public interface BoardGUI {
  public void draw(); // draws a ParkGUI object (either an animal or a button) to the display window

  public void mousePressed(); // called each time the mouse is Pressed

  public void mouseReleased(); // called each time the mouse is Pressed

  public boolean isMouseOver(); // checks whether the mouse is over a ParkGUI object

}
