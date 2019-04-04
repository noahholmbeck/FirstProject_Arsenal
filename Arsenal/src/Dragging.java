
public class Dragging {

  private int hasBeenDragging;
  
  public Dragging() {
    hasBeenDragging = 0;
  }
  
  public void set(int dragging) {
    hasBeenDragging = dragging;
  }
  
  public int get() {
    return hasBeenDragging;
  }
  
  public void increment() {
    if (hasBeenDragging >= 1) {
      hasBeenDragging = 2;
    }
    if (hasBeenDragging == 0)
      hasBeenDragging++;
  }
}
