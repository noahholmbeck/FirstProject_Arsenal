
public class PassButton extends Button {

  public PassButton(GameBoard processing, PImage image, int x, int y, int messageX, int messageY) {
    super(processing, image, x, y, messageX, messageY);
  }
  
  public void press() {
    takeAction();
  }

  @Override
  protected void takeAction() {
    dualButtonMessage = "";
    
    
    if(processing.someoneHasAddedTo14()) {
      
      processing.updateWorstTheif();
      if(processing.getDisplayScreen() == (DisplayScreen.STEEL)) {
        
        if(processing.theifHasChosen()) {
          processing.stealCard();
          if(!processing.someoneHasAddedTo14()) {
            processing.setNewRound();
          } else {
            processing.updateWorstTheif();
          }
          processing.updateDisplayScreen(DisplayScreen.PLAYER);
        } else {
          Button.textShift = Button.TEXT_VARIANCE;
          processing.takePointButton.clearPointMessage();
          dualButtonMessage = "Must select one card to steal";
        }
        
      } else {
        if (processing.getDisplayScreen() != DisplayScreen.BADTHEIVES) {
          processing.updateDisplayScreen(DisplayScreen.PLAYER);
        }
      }
      
    } else {
      if (processing.arsenalSwap) {
        int swapResult = processing.validAddSwap();
        if (swapResult < 0) {
          Button.textShift = Button.TEXT_VARIANCE;
          dualButtonMessage =
              (swapResult == -1) ? "Number of arsenal cards cannot\nincrease during this phase"
                  : "Hand can only contain five cards";
        } else {
          processing.playerPassed();
        }
      } else {
        if (!processing.validStacks()) {
          Button.textShift = Button.TEXT_VARIANCE;
          dualButtonMessage = "Card stacks must add\nto 5, 10, 14, 15, or 20";
        } else {
          processing.playerPassed();
        }
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
