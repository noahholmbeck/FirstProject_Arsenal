
public class Card {

  private GameBoard processing;
  private int value; // 1-13, 0 = joker
  private char suit; // 'H','D','C','S'... 'W' = joker
  private PImage image;
  private static final int POWER_VALUE = 3;
  private static final int MENIAL_VALUE = 1;

  public Card(int rank, char suit) {
    this(null, rank, suit);
  }
  
  public Card(GameBoard processing, int rank, char suit) {

    this.value = rank;
    this.suit = suit;
    this.processing = processing;
    String fileName;

    if(processing != null) {
      switch (rank) {
        case 1:
          fileName = "A" + this.suit;
          break;
        case 11:
          fileName = "J" + this.suit;
          break;
        case 12:
          fileName = "Q" + this.suit;
          break;
        case 13:
          fileName = "K" + this.suit;
          break;
        case 0:
          fileName = "yellow_back";
          break;
        default:
          fileName = Integer.toString(rank) + this.suit;
          break;
      }
      
      fileName += ".png";
      image = processing.loadImage("images/" + fileName);
      processing.scaleCard(image);
    }
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Card) {
      Card card = (Card) object;
      if (card.getSuit() == this.getSuit() && card.getValue() == this.getValue()) {
        return true;
      }
    }
    return false;
  }

  public char getSuit() {
    return suit;
  }

  public int getValue() {
    return value;
  }

  public PImage getImage() {
    return image;
  }

  public int compareTo(Card card) {
    int thisCardVal = this.value * 10 + 4 - this.suitIntValue(suit);
    int thatCardVal = card.getValue() * 10 + 4 - this.suitIntValue(card.getSuit());
    if (thisCardVal > thatCardVal) {
      return 1;
    } else if (thisCardVal < thatCardVal) {
      return -1;
    } else {
      return 0;
    }
  }

  // returns W = 1, H = 2, D = 3, C = 4, S = 5, -1 if invalid
  public int suitIntValue(char suit) {
    if (suit != 'H' && suit != 'D' && suit != 'C' && suit != 'S' && suit != 'W') {
      throw new IllegalArgumentException("invalid suit to be converted to an int value.");
    }
    switch (suit) {
      case 'H':
        return 0;
      case 'D':
        return 1;
      case 'C':
        return 2;
      case 'S':
        return 3;
      case 'W':
        return 4;
      default:
        return -1;
    }
  }

  public String toString() {
    return "" + this.value + " of " + this.suit;
  }

  public static int scoreOfCard(Card scoreCard) {
    Card tenOfSpades = new Card(10, 'S');
    if(scoreCard.compareTo(tenOfSpades) < 0) {
      return Card.MENIAL_VALUE;
    }
    return Card.POWER_VALUE;
  }
  
  public static int getPowerVal() {
    return Card.POWER_VALUE;
  }
  
  public static int getMenialVal() {
    return Card.MENIAL_VALUE;
  }

}
