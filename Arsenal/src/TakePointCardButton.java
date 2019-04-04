
public class TakePointCardButton extends Button {

  public TakePointCardButton(GameBoard processing, PImage image, int x, int y, int messageX, int messageY) {
    super(processing, image, x, y, messageX, messageY);
  }

  @Override
  protected void takeAction() {
    steelMessage = "";
    
    if (!this.processing.theifTakesPointCard()) {
      Button.textShift = Button.TEXT_VARIANCE;
      steelMessage = "opponent has no singe pointers";
      Button.dualButtonMessage = "";
    } else {
      steelMessage = "";
      if(!processing.someoneHasAddedTo14()) {
        processing.setNewRound();
      } else {
        processing.updateWorstTheif();
        processing.updateDisplayScreen(DisplayScreen.PLAYER);
      }
    }
  }

  @Override
  public void printCorrectMessage() {
    if(steelMessage != "") {
      displayMessage(steelMessage);
    }    
  }
  
}
