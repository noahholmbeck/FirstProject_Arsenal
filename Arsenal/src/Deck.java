import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DataFormatException;

public class Deck implements BoardGUI {

  protected String deckName;
  protected ArrayList<Card> cards;
  protected GameBoard processing;
  protected int[] position;

  private int emptyOutlineWeight;
  private int emptyXWeight;

  private int[] topLeft;
  private int[] topRight;
  private int[] bottomLeft;
  private int[] bottomRight;



  public Deck() {
    this.processing = null;
    this.position = null;
    this.cards = null;
    this.deckName = "uninitialized";
  }



  public Deck(GameBoard processing) {
    this(processing, "Full Deck", PApplet.CENTER, PApplet.CENTER, 1, 13, true);
  }



  public Deck(GameBoard processing, String deckName, int xPosition, int yPosition, int lowVal,
      int highVal, boolean jokers) {

    if (lowVal < 1 || lowVal > 13 || highVal > 13 || highVal < 1 || highVal < lowVal) {
      throw new IllegalArgumentException("invalid high and low values entered");
    }

    this.processing = processing;
    this.position = new int[] {xPosition, yPosition};
    this.cards = new ArrayList<>();
    this.deckName = deckName;
    shuffle();

    calcCorners();

    for (int value = lowVal; value <= highVal; ++value) {
      cards.add(new Card(processing, value, 'H'));
      cards.add(new Card(processing, value, 'D'));
      cards.add(new Card(processing, value, 'C'));
      cards.add(new Card(processing, value, 'S'));
    }
    if (jokers == true) {
      cards.add(new Card(processing, 0, 'W'));
      cards.add(new Card(processing, 0, 'W'));
    }
    shuffle();
  }



  public Deck(GameBoard processing, String deckName, int xPosition, int yPosition,
      ArrayList<Card> cards) {

    this.processing = processing;
    this.deckName = deckName;
    this.cards = cards;
    this.position = new int[] {xPosition, yPosition};
    calcCorners();
    shuffle();
  }


  /**
   * Draws the animal to the display window. It sets also its position to the mouse position if the
   * tiger is being dragged (i.e. if its isDragging field is set to true).
   */
  @Override
  public void draw() {

    if (cards.size() > 0) {
      this.processing.image(processing.cardBack, this.position[0], position[1]);
    } else {
      this.displayDeckOutline();
      this.displayDeckX();
    }
    // display label
    displayLabel();
  }

  /**
   * display's the Tiger object label on the application window screen
   */
  private void displayLabel() {
    this.processing.fill(255); // specify font color: white
    this.processing.textSize(20);
    this.processing.text(this.deckName, this.position[0],
        this.position[1] + processing.cardBack.height / 2 + 13);// display
    // label
    // //
    // text
  }

  public int size() {
    return cards.size();
  }

  private void shuffle() {
    Collections.shuffle(cards);
  }

  public Card pick() {
    return cards.remove(cards.size() - 1);
  }

  public ArrayList<Card> cards() {
    return this.cards;
  }

  @Override
  public String toString() {
    return this.deckName;
  }

  protected void displayDeckOutline() {
    processing.stroke(processing.EMPTY_STROKE_COLOR[0], processing.EMPTY_STROKE_COLOR[1],
        processing.EMPTY_STROKE_COLOR[2]);
    processing.strokeWeight(emptyOutlineWeight);
    processing.noFill();
    processing.rectMode(processing.CENTER);
    processing.rect(position[0], position[1], processing.cardBack.width, processing.cardBack.height,
        8);
    processing.noStroke();
  }

  protected void displayDeckX() {
    processing.stroke(processing.EMPTY_X_STROKE_COLOR[0], processing.EMPTY_X_STROKE_COLOR[1],
        processing.EMPTY_X_STROKE_COLOR[2]);
    processing.strokeWeight(this.emptyXWeight);
    processing.line(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]);
    processing.line(topRight[0], topRight[1], bottomLeft[0], bottomLeft[1]);
    processing.noStroke();
  }

  /*
   * prints message if mouse is pressed over the button
   * 
   * @see ParkGUI#mousePressed()
   */
  @Override
  public void mousePressed() {

  }

  @Override
  public void mouseReleased() {

  }

  /*
   * determines if mouse is over the button
   * 
   * @return boolean true if mouse is over the button
   * 
   * @see ParkGUI#isMouseOver()
   */
  @Override
  public boolean isMouseOver() {
    // if the mouse is between the top left and bottom right corners of button, true returned
    if (this.processing.mouseX > this.position[0] - processing.cardBack.width / 2
        && this.processing.mouseX < this.position[0] + processing.cardBack.width / 2
        && this.processing.mouseY > this.position[1] - processing.cardBack.height / 2
        && this.processing.mouseY < this.position[1] + processing.cardBack.height / 2)
      return true;
    return false;
  }

  private void calcCorners() {
    this.emptyOutlineWeight = (int) (processing.EMPTY_STROKE_SCALE * processing.width);
    this.emptyXWeight = (int) (processing.EMPTY_X_STROKE_SCALE * processing.width);
    this.topLeft = new int[] {position[0] - processing.cardBack.width / 2 + processing.STROKE_RADIUS / 2,
        position[1] - processing.cardBack.height / 2 + processing.STROKE_RADIUS / 2};
    this.topRight = new int[] {position[0] + processing.cardBack.width / 2 - processing.STROKE_RADIUS / 2,
        position[1] - processing.cardBack.height / 2 + processing.STROKE_RADIUS / 2};
    this.bottomLeft = new int[] {position[0] - processing.cardBack.width / 2 + processing.STROKE_RADIUS / 2,
        position[1] + processing.cardBack.height / 2 - processing.STROKE_RADIUS / 2};
    this.bottomRight = new int[] {position[0] + processing.cardBack.width / 2 - processing.STROKE_RADIUS / 2,
        position[1] + processing.cardBack.height / 2 - processing.STROKE_RADIUS / 2};
  }
  
  public static int numPowerCards(ArrayList<Card> cardsToSearch) {
    int powerCount = 0;
    for (int i = 0; i < cardsToSearch.size(); ++i) {
      if (Card.scoreOfCard(cardsToSearch.get(i)) == Card.getPowerVal()) {
        powerCount++;
      }
    }
    return powerCount;
  }
  
  public static int totalFaceValueMenialCards(ArrayList<Card> cardsToSearch) {
    int totalFaceValue = 0;
    for (int i = 0; i < cardsToSearch.size(); ++i) {
      if (Card.scoreOfCard(cardsToSearch.get(i)) == Card.getMenialVal()) {
        totalFaceValue += cardsToSearch.get(i).getValue();
      }
    }
    return totalFaceValue;
  }

}
