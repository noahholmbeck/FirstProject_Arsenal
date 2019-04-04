import java.util.ArrayList;


public class ArsenalDeck extends Deck {

  private boolean displayed;
  protected ArrayList<Card> selectedCards;
  private boolean selectable;


  public ArsenalDeck(GameBoard processing, String deckName, int xPosition, int yPosition) {
    super(processing, deckName, xPosition, yPosition, new ArrayList<Card>());
    this.displayed = false;
    this.selectedCards = new ArrayList<>();
    this.selectable = false;
  }

  public void add(Card cardToAdd) {
    cards.add(cardToAdd);
  }
  
  public Card remove(int index) {
    return this.cards.remove(index);
  }
  
  public int indexOf(Card card) {
    for(int i = 0; i < cards.size(); ++i) {
      if(cards.get(i).equals(card)) {
        return i;
      }
    }
    return -1;
  }

  public boolean isDisplayed() {
    return displayed;
  }

  public void reveal() {
    displayed = true;
  }

  public void hide() {
    displayed = false;
    selectedCards.clear();
  }

  public void selectable() {
    selectable = true;
  }

  public void notSelectable() {
    selectable = false;
  }
  
  public ArrayList<Card> getCards() {
    return this.cards;
  }
  
  public void draw(boolean mousePressed) {
    if (!cards.isEmpty()) {
      if (displayed) {
        displayDeckOutline();
        drawDeck();
        if(selectable) {
          processing.text("Select any/all arsenal cards to swap/add",
              processing.width/2, processing.height/100*90);
        }
        if (mousePressed && selectable) {
          Card cardUnderMouse = getCardUnderMouse();
          if (cardUnderMouse != null) {
            if (selectedCards.contains(cardUnderMouse)) {
              selectedCards.remove(cardUnderMouse);
            } else {
              selectedCards.add(cardUnderMouse);
            }
          }
        }
      } else {
        processing.image(processing.cardBack, position[0], position[1]);
      }
    } else {
      displayDeckOutline();
      displayDeckX();
      if (selectable) {
        processing.text("You have no arsenal cards to swap.",
            position[0] + processing.textWidth("You have no arsenal cards to swap")/30*26, position[1]);
      }
    }
  }

  private void drawDeck() {
    float y = position[1];
    float x = position[0];
    PImage image;
    for (int i = 0; i < cards.size(); i++) {
      x += (i == 0) ? processing.middleSeperation : processing.splaySeperation;
      image = cards.get(i).getImage();
      processing.image(image, x, y);
      if (selectedCards.contains(cards.get(i))) {
        processing.stroke(processing.ARSENAL_SELECTED_STROKE_COLOR[0],
            processing.ARSENAL_SELECTED_STROKE_COLOR[1],
            processing.ARSENAL_SELECTED_STROKE_COLOR[2]);
        processing.strokeWeight(processing.selectedStrokeWeight);
        processing.noFill();
        processing.rectMode(processing.CENTER);
        processing.rect(x, y, image.width, image.height, processing.STROKE_RADIUS);
        processing.noStroke();
      }
    }
  }


  public Card getCardUnderMouse() {
    if (displayed) {
      for (int i = cards.size() - 1; i >= 0; i--) {
        if (this.processing.mouseX > ((processing.middleSeperation + this.position[0]
            + (i) * processing.splaySeperation) - processing.cardBack.width / 2)
            && this.processing.mouseX < ((processing.middleSeperation + this.position[0]
                + (i) * processing.splaySeperation) + processing.cardBack.width / 2)
            && this.processing.mouseY > (this.position[1] - processing.cardBack.height / 2)
            && this.processing.mouseY < (this.position[1] + processing.cardBack.height / 2))
          return cards.get(i);
      }
    }
    return null;
  }


}
