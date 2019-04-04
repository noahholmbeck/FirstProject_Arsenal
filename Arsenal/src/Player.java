import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class Player implements BoardGUI {

  private final Dragging DRAGGING = new Dragging();

  private String name;
  private ArrayList<Card> hand;
  private ArsenalDeck arsenal;
  private Card[][] cardsPlayed;
  private Card[][] cardsPlaying;
  private ArrayList<Card> hiddenPlaying;
  private GameBoard processing;
  private boolean[] showCards;
  private int handIndexSelected;
  private Card movingCard;
  private int[] cardLocation;
  private int playedIndex;
  private ArrayList<Card> cardsScored;
  private boolean hasPassed;

  protected ArrayList<Card> selectedCards;

  public Player(GameBoard processing) {
    this.name = "New Player";
    this.hand = new ArrayList<>();
    this.arsenal = new ArsenalDeck(processing, name + " Arsenal",
        processing.currentPlayerPosition[0],
        processing.currentPlayerPosition[1] - processing.cardBack.height - processing.ySeperation);
    this.processing = processing;
    this.cardsPlayed = new Card[4][5];
    this.cardsPlaying = new Card[4][5];
    this.hiddenPlaying = new ArrayList<>();
    this.handIndexSelected = -1;
    this.movingCard = null;
    this.cardLocation = new int[2];
    this.playedIndex = -1;
    this.showCards = new boolean[processing.HAND_SIZE];
    for (int i = 0; i < showCards.length; ++i) {
      showCards[i] = true;
    }
    this.cardsScored = new ArrayList<>();
    this.selectedCards = new ArrayList<>();
  }

  public int cardsCount() {
    return hand.size();
  }

  public ArrayList<Card> getCardsScored() {
    return cardsScored;
  }

  public void pick(Deck[] decks) {
//    System.out.println("Player picked.");
    Card joker = new Card(processing, 0, 'W');
    if (hand.size() < GameBoard.HAND_SIZE) {
      Card picked;
      for (int i = (decks.length - 3); i >= 0; i--) {
        if (decks[i].cards().size() > 0) {
          picked = decks[i].pick();
          if (picked.compareTo(joker) != 0) {
            hand.add(picked);
            return;
          } else {
            processing.jokerPicked(false);
            pick(decks);
            return;
          }
        }
      }
    }
  }


  protected void displayCurrentPlayer() {


    if (processing.arsenalSwap) {
      arsenal.selectable();
      this.displayArsenalSwap();
    } else {
      arsenal.notSelectable();
      this.displayBattle();
    }

    displayScore(processing.currentScorePosition[0], processing.currentScorePosition[1], true);
  }


  private void displayArsenalSwap() {
    arsenal.reveal();

    if (processing.mousePressed) {
      DRAGGING.increment();
    } else {
      DRAGGING.set(0);
    }
    if (DRAGGING.get() == 1) {
      arsenal.draw(true);
      this.drawSelectedHand(true);
    } else {
      arsenal.draw(false);
      this.drawSelectedHand(false);
    }

  }

  private void drawSelectedHand(boolean mousePressed) {
    if (mousePressed) {
      Card cardUnderMouse = cardUnderMouse();
      if (cardUnderMouse != null) {
        if (selectedCards.contains(cardUnderMouse)) {
          selectedCards.remove(cardUnderMouse);
        } else {
          selectedCards.add(cardUnderMouse);
        }
      }
    }
    int x;
    int y = processing.currentPlayerPosition[1];
    PImage image;
    for (int i = 0; i < hand.size(); i++) {
      x = processing.currentPlayerPosition[0] + i * processing.middleSeperation;
      image = hand.get(i).getImage();
      processing.image(image, x, y);
      if (selectedCards.contains(hand.get(i))) {
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

  private void displayBattle() {
    calcShowCards();
    for (int i = 0; i < hand.size(); i++) {
      if (!hand.get(i).equals(movingCard) && showCards[i])
        processing.image(hand.get(i).getImage(),
            processing.currentPlayerPosition[0] + i * processing.middleSeperation,
            processing.currentPlayerPosition[1]);
    }


    for (int i = 0; i < cardsPlayed.length; i++) {
      int j = 0;
      for (j = 0; j < cardsPlayed[i].length; ++j) {
        if (cardsPlayed[i][j] != null) {
          processing.image(cardsPlayed[i][j].getImage(),
              processing.middle2X + i * processing.middleSeperation,
              processing.playedYPosition + j * processing.ySeperation);
        } else {
          break;
        }
      }
      PImage image;
      int x;
      int y;
      for (int k = 0; k < cardsPlaying[i].length; ++k) {
        if (cardsPlaying[i][k] != null) {
          image = cardsPlaying[i][k].getImage();
          x = processing.middle2X + i * processing.middleSeperation;
          y = processing.playedYPosition + j * processing.ySeperation;
          processing.image(image, x, y);
          processing.stroke(processing.ARSENAL_SELECTED_STROKE_COLOR[0],
              processing.ARSENAL_SELECTED_STROKE_COLOR[1],
              processing.ARSENAL_SELECTED_STROKE_COLOR[2]);
          processing.strokeWeight(processing.selectedStrokeWeight);
          processing.noFill();
          processing.rectMode(processing.CENTER);
          processing.rect(x, y, image.width, image.height, processing.STROKE_RADIUS);
          processing.noStroke();
          j++;
        }
      }
    }


    if (DRAGGING.get() == 1) {
      arsenal.draw(true);
    } else {
      arsenal.draw(false);
    }
    if (processing.mousePressed) {
      DRAGGING.increment();
      mousePressed();
    } else {
      mouseReleased();
    }

  }


  protected void displayHiddenPlayer() {
    for (int i = 0; i < hand.size(); i++) {
      processing.image(processing.cardBack,
          processing.hiddenPlayerPosition[0] + i * (int) (processing.displayWidth / 65),
          processing.hiddenPlayerPosition[1] - i * (int) (processing.displayHeight / 85));
    }

    PImage image = processing.cardBack;
    for (int i = 0; i < cardsPlayed.length; ++i) {
      for (int j = 0; j < cardsPlayed[i].length; ++j) {
        int x = processing.middle2X + i * processing.middleSeperation;
        int y = (int) (processing.hiddenPlayedYPosition - j * processing.ySeperation * 0.34);
        // if (cardsPlayed[i][j] != null) {
        // processing.image(processing.cardBack,
        // processing.middle2X + i * processing.middleSeperation,
        // (int) (processing.hiddenPlayedYPosition - j * processing.ySeperation * 0.34));
        // }
        if (cardsPlayed[i][j] != null) {
          processing.image(image, x, y);
          if ((processing.numPlayersPassed() == 0) && hiddenPlaying.contains(cardsPlayed[i][j])) {
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
    }
    displayScore(processing.hiddenPlayerScorePosition[0], processing.hiddenPlayerScorePosition[1], false);
  }

  public boolean handContains(Card card) {
    for (Card thisCard : hand) {
      if (thisCard.equals(card))
        return true;
    }
    return false;
  }

  public boolean arrContains(Card[][] cards, Card card) {
    for (int i = 0; i < cards.length; ++i) {
      for (int j = 0; j < cards[i].length; ++j) {
        if (cards[i][j] != null && cards[i][j].equals(card))
          return true;
      }
    }
    return false;
  }

  // returns int[0] = same suit count int[1] = of suit = h,d,c,s = 4,3,2,1 relatively
  public int[] sameSuitInHand() {

    int[] suitsCount = suitsCount(hand);
    int hearts = suitsCount[0];
    int diamonds = suitsCount[1];
    int clubs = suitsCount[2];
    int spades = suitsCount[3];


    if (hearts >= diamonds && hearts >= clubs && hearts >= spades) {
      return new int[] {hearts, 4};
    }
    if (diamonds >= hearts && diamonds >= clubs && diamonds >= spades) {
      return new int[] {diamonds, 3};
    }
    if (clubs >= diamonds && clubs >= hearts && clubs >= spades) {
      return new int[] {clubs, 2};
    }
    if (spades >= diamonds && spades >= clubs && spades >= hearts) {
      return new int[] {spades, 1};
    }
    return new int[] {-1, -1};
  }


  public int[] suitsCount(ArrayList<Card> cards) {
    int hearts = 0;
    int diamonds = 0;
    int clubs = 0;
    int spades = 0;
    for (int i = 0; i < cards.size(); i++) {
      switch (cards.get(i).getSuit()) {
        case 'H':
          hearts++;
          break;
        case 'D':
          diamonds++;
          break;
        case 'C':
          clubs++;
          break;
        case 'S':
          spades++;
          break;
      }
    }
    return new int[] {hearts, diamonds, clubs, spades};
  }


  private int indexUnderMouse() {
    int x = processing.mouseX;
    int y = processing.mouseY;
    int width;
    int height;
    int xMin;
    int xMax;
    int yMin;
    int yMax;
    for (int i = 0; i < hand.size(); ++i) {
      if (showCards[i] == true) {
        width = hand.get(i).getImage().width;
        height = hand.get(i).getImage().height;
        xMin = processing.currentPlayerPosition[0] + i * processing.middleSeperation - width / 2;
        xMax = processing.currentPlayerPosition[0] + i * processing.middleSeperation + width / 2;
        yMin = processing.currentPlayerPosition[1] - height / 2;
        yMax = processing.currentPlayerPosition[1] + height / 2;
        if (x > xMin && x < xMax && y > yMin && y < yMax) {
          return i;
        }
      }
    }
    return -1;
  }


  private Card cardUnderMouse() {
    int x = processing.mouseX;
    int y = processing.mouseY;
    int width;
    int height;
    int xMin;
    int xMax;
    int yMin;
    int yMax;
    for (int i = 0; i < hand.size(); ++i) {
      width = hand.get(i).getImage().width;
      height = hand.get(i).getImage().height;
      xMin = processing.currentPlayerPosition[0] + i * processing.middleSeperation - width / 2;
      xMax = processing.currentPlayerPosition[0] + i * processing.middleSeperation + width / 2;
      yMin = processing.currentPlayerPosition[1] - height / 2;
      yMax = processing.currentPlayerPosition[1] + height / 2;
      if (x > xMin && x < xMax && y > yMin && y < yMax) {
        return hand.get(i);
      }
    }
    return null;
  }


  @Override
  public void mousePressed() {
    if (arsenal.isMouseOver() && DRAGGING.get() == 1) {
      if (!arsenal.isDisplayed()) {
        arsenal.reveal();
      } else {
        arsenal.hide();
      }
    } else {
      if (indexUnderMouse() != -1 && DRAGGING.get() == 1) {

        handIndexSelected = indexUnderMouse();
        movingCard = hand.get(handIndexSelected);
      } else {
        int playedIndex = processing.playableLocation(processing.mouseX, processing.mouseY);
        if (playedIndex != -1 && DRAGGING.get() == 1) {
          for (int i = 0; i < cardsPlaying[playedIndex].length; ++i) {
            cardsPlaying[playedIndex][i] = null;
            cardLocation = new int[2];
          }
        }
      }
      if (DRAGGING.get() > 0 && handIndexSelected != -1) {
        cardLocation[0] = processing.mouseX;
        cardLocation[1] = processing.mouseY;
        processing.image(movingCard.getImage(), cardLocation[0], cardLocation[1]);
      }
    }
  }


  @Override
  public void mouseReleased() {
    DRAGGING.set(0);
    if (handIndexSelected != -1) {
      Card fakeJoker = new Card(processing, 0, 'W');
      movingCard = null;
      playedIndex = processing.playableLocation(cardLocation[0], cardLocation[1]);
      if ((playedIndex != -1) && (!arrContains(cardsPlaying, hand.get(handIndexSelected)))
          && processing.middleCardAt(playedIndex).compareTo(fakeJoker) != 0) {
        for (int i = 0; i < cardsPlaying[playedIndex].length; i++) {
          if (cardsPlaying[playedIndex][i] == null) {
            cardsPlaying[playedIndex][i] = hand.get(handIndexSelected);
            if ((processing.NUM_PLAYERS - processing.numPlayersPassed() > 1)
                && !playedOnOneOrLessStacks()) {
              cardsPlaying[playedIndex][i] = null;
              break;
            }
            break;
          }
        }
      }
      handIndexSelected = -1;
    }
  }


  public double[] getStackLengths() {
    double[] lengths = new double[4];
    for (int i = 0; i < cardsPlayed.length; i++) {
      for (int j = 0; j <= cardsPlayed[i].length; j++) {
        if (j == cardsPlayed[i].length || cardsPlayed[i][j] == null) {
          lengths[i] = j + 0.56;
          break;
        }
      }
    }
    for (int i = 0; i < cardsPlaying.length; i++) {
      for (int j = 0; j <= cardsPlaying[i].length; j++) {
        if (j == cardsPlaying[i].length || cardsPlaying[i][j] == null) {
          lengths[i] += j;
          break;
        }
      }
    }
    return lengths;
  }


  private void calcShowCards() {
    Arrays.fill(showCards, true);
    for (int i = 0; i < hand.size(); i++) {
      if (arrContains(cardsPlaying, hand.get(i))) {
        showCards[i] = false;
      }
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }


  public void save() {
    for (int i = 0; i < hand.size(); ++i) {
      if (arrContains(cardsPlaying, hand.get(i))) {
        hand.remove(i);
        i--;
      }
    }
    hiddenPlaying.clear();
    for (int i = 0; i < cardsPlaying.length; ++i) {
      for (int j = 0; j < cardsPlaying[i].length; ++j) {
        hiddenPlaying.add(cardsPlaying[i][j]);
        if (cardsPlaying[i][j] != null) {
          for (int k = 0; k < cardsPlayed[i].length; ++k) {
            if (cardsPlayed[i][k] == null) {
              cardsPlayed[i][k] = cardsPlaying[i][j];
              break;
            }
          }
        }
      }
    }
    clear2DCardArray(cardsPlaying);
  }


  public boolean hasPlayed() {
    for (int i = 0; i < cardsPlaying.length; ++i) {
      for (int j = 0; j < cardsPlaying[i].length; ++j) {
        if (cardsPlaying[i][j] != null) {
          return true;
        }
      }
    }
    return false;
  }


  public boolean validStacks() {

    boolean validStacks = false;
    int total = 0;
    Card middleCard;
    ArrayList<Integer> stack = new ArrayList<>();

    Card wildComparison = new Card(processing, 0, 'W');

    for (int index = 0; index < 4; ++index) {
      total = 0;
      middleCard = processing.middleCardAt(index);
      stack.clear();

      // if middle is wild and there was a card played on it, not valid stack
      if (middleCard.compareTo(wildComparison) == 0
          && (cardsPlayed[index][0] != null || cardsPlaying[index][0] != null)) {
        validStacks = false;
        return validStacks;
      }

      // all cards played on a given middle card are added to the stack
      for (int i = 0; i < cardsPlayed[index].length; ++i) {
        if (cardsPlayed[index][i] == null) {
          break;
        }
        stack.add(cardsPlayed[index][i].getValue());
      }
      // all cards currently being played on a given stack are added to the stack
      for (int i = 0; i < cardsPlaying[index].length; ++i) {
        if (cardsPlaying[index][i] == null) {
          break;
        }
        stack.add(cardsPlaying[index][i].getValue());
      }

      // gets the stack total excluding the middle card
      for (int i = 0; i < stack.size(); ++i) {
        total += stack.get(i);
      }
      // System.out.println("player stack: " + total);

      // if the only card played on the middle card is the same value as said middle card, stack is
      // valid
      if (total == middleCard.getValue() && stack.size() == 1) {
        validStacks = true;
        return validStacks;
      }
      // middle card is summed with player stack
      total += middleCard.getValue();
      // System.out.println("with middle: " + total);
      // if the stack total is valid, said validity is returned
      if ((total <= 20 && total % 5 == 0) || (total == 14) || (total == middleCard.getValue())) {
        validStacks = true;
      } else {
        validStacks = false;
        return validStacks;
      }
    }
    return validStacks;
  }

  public int validAddSwap() {
    int returnVal;
    if (selectedCards.size() <= arsenal.selectedCards.size()) {
      if (hand.size() < 5 || this.selectedCards.size() == arsenal.selectedCards.size()) {
        for (int i = 0; i < this.selectedCards.size(); i++) {
          arsenal.add(this.selectedCards.get(i));
          hand.remove(hand.indexOf(this.selectedCards.get(i)));
        }
        selectedCards.clear();
        for (int i = 0; i < arsenal.selectedCards.size(); i++) {
          hand.add(arsenal.selectedCards.get(i));
          arsenal.remove(arsenal.indexOf(arsenal.selectedCards.get(i)));
        }
        arsenal.selectedCards.clear();
        // if successful
        returnVal = 1;
      } else {
        // if hand limit exceeded
        returnVal = -2;
      }
    } else {
      // if added more cards to arsenal than cards taken
      returnVal = -1;
    }
    return returnVal;
  }

  public void clear2DCardArray(Card[][] cards) {
    for (int i = 0; i < cards.length; ++i) {
      for (int j = 0; j < cards[i].length; ++j) {
        cards[i][j] = null;
      }
    }
  }

  public void addToArsenal(Card cardToAdd) {
    arsenal.add(cardToAdd);
  }

  public void scored(Card scoredCard) {
    cardsScored.add(scoredCard);
  }


  public int calculateScore() {
    int totalScore = 0;
    for (int i = 0; i < cardsScored.size(); ++i) {
      totalScore += Card.scoreOfCard(cardsScored.get(i));
    }
    return totalScore;
  }

  public int displayScore(int x, int y, boolean currentPlayer) {
    int yToReturn;
    y = (currentPlayer) ? y - (cardsScored.size() * processing.ySeperation)
        : y + (cardsScored.size() * processing.ySeperation);
    yToReturn = y;
    for (int i = cardsScored.size() - 1; i >= 0; --i) {
      y = (currentPlayer) ? y + processing.ySeperation : y - processing.ySeperation;
      processing.image(cardsScored.get(i).getImage(), x, y);
    }
    return yToReturn;
  }

  public boolean playedOnOneOrLessStacks() {
    int numberOfStacks = 0;
    for (int i = 0; i < cardsPlaying.length; i++) {
      if (cardsPlaying[i][0] != null) {
        numberOfStacks++;
      }
    }
    return (numberOfStacks <= 1);
  }

  // not used at the moment
  public boolean validStack(int index) {
    Card[] stack = cardsPlaying[index];
    int middleVal = processing.middleCardAt(index).getValue();
    int total = 0;
    int numPlayed = 0;
    for (int i = 0; i < stack.length; ++i) {
      if (stack[i] == null) {
        numPlayed = i;
        break;
      }
      total += stack[i].getValue();
    }
    if (numPlayed == 1 && total == middleVal) {
      return true;
    }
    total += middleVal;
    if ((total <= 20 && total % 5 == 0) || total == 14) {
      return true;
    }
    return false;
  }

  public Card[][] getPlayed() {
    return cardsPlayed;
  }

  public ArsenalDeck getArsenal() {
    return arsenal;
  }

  public boolean hasPassed() {
    return this.hasPassed;
  }

  public void setPassed(boolean hasPassed) {
    this.hasPassed = hasPassed;
  }

  @Override
  public boolean isMouseOver() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void draw() {
    // TODO Auto-generated method stub

  }
}
