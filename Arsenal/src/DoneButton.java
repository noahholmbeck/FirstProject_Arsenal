
public class DoneButton extends Button {

  boolean alreadyPressed;

  public DoneButton(GameBoard processing, PImage image, int x, int y, int messageX, int messageY) {
    super(processing, image, x, y, messageX, messageY);
    alreadyPressed = false;
  }

  @Override
  protected void takeAction() {
    dualButtonMessage = "";
    if (!processing.validStacks()) {
      Button.textShift = Button.TEXT_VARIANCE;
      Button.dualButtonMessage = "Card stacks must add\nto 5, 10, 14, 15, or 20";
      alreadyPressed = false;
    } else {
      alreadyPressed = !this.processing.playerDone(alreadyPressed);
      if (alreadyPressed) {
        Button.textShift = Button.TEXT_VARIANCE;
        Button.dualButtonMessage = "No cards have been played.\nPlay cards or press again to pass.";
      } else {
        steelMessage = "";
      }
    }
  }

  @Override
  public void printCorrectMessage() {
    if(dualButtonMessage != "") {
      displayMessage(Button.dualButtonMessage);
    }    
  }
}
