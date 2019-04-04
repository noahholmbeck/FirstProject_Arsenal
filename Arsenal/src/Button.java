
public abstract class Button implements BoardGUI {

  private final Dragging DRAGGING = new Dragging();
  private int xPosition;
  private int yPosition;
  private int messageX;
  private int messageY;
  private PImage image;
  protected GameBoard processing;
  protected static String dualButtonMessage;
  protected String steelMessage;
  private final static int TEXT_NORMAL = 0;
  protected final static int TEXT_VARIANCE = 255;
  private final static int TEXT_RATE = 4;
  protected static int textShift;

  public Button(GameBoard processing, PImage image, int x, int y, int messageX, int messageY) {
    this.xPosition = x;
    this.yPosition = y;
    this.messageX = messageX;
    this.messageY = messageY;
    this.image = image;
    this.processing = processing;
    steelMessage = "";
    Button.dualButtonMessage = "";
    Button.textShift = Button.TEXT_NORMAL;
  }

  @Override
  public void draw() {
    processing.image(image, this.xPosition, this.yPosition);
    if (processing.mousePressed) {
      DRAGGING.increment();
      mousePressed();
    }
    if (!processing.mousePressed) {
      mouseReleased();
    }
    printCorrectMessage();
  }

  public abstract void printCorrectMessage();
  
  protected void displayMessage(String messageToDraw) {
    processing.textSize(22);
    processing.fill(Button.TEXT_NORMAL, Button.TEXT_NORMAL, Button.TEXT_NORMAL);
    if ((Button.textShift - Button.TEXT_NORMAL) > Button.TEXT_RATE
        || (Button.TEXT_NORMAL - Button.textShift) > Button.TEXT_RATE) {
      processing.fill(Button.textShift, Button.textShift, Button.textShift);
      textShift = (TEXT_NORMAL > TEXT_VARIANCE) ? Button.textShift + Button.TEXT_RATE
          : textShift - Button.TEXT_RATE;
    }
    processing.text(messageToDraw, messageX, messageY);
  }

  @Override
  public void mousePressed() {
    if (isMouseOver() && DRAGGING.get() == 1) {
      takeAction();
    }

  }

  @Override
  public void mouseReleased() {
    DRAGGING.set(0);
  }

  @Override
  public boolean isMouseOver() {
    if (processing.mouseX > this.xPosition - image.width / 2
        && processing.mouseX < this.xPosition + image.width / 2
        && processing.mouseY > this.yPosition - image.height / 2
        && processing.mouseY < this.yPosition + image.width / 2)
      return true;
    return false;
  }
  
  public void clearPointMessage() {
    steelMessage = "";
  }
  

  protected abstract void takeAction();


}
