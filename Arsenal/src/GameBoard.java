import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard extends PApplet {

  private final Dragging DRAGGING = new Dragging();

  private PImage backgroundImage; // PImage object that represents the background image

  private final String CARD_BACK = "images/gray_back.png";
  protected PImage cardBack;
  private final double CARD_IMAGE_SCALE = 0.15;
  private int cardImageHeight;
  private PImage doneImage;
  private PImage passImage;
  private PImage takeImage;
  private double DONE_IMAGE_SCALE = 0.0002;
  private double PASS_IMAGE_SCALE = 0.000047;
  private double TAKE_IMAGE_SCALE = 0.000047;
  private double doneImageScale;
  private double passImageScale;
  private double takeImageScale;

  private DoneButton doneButton;
  protected PassButton passButton;
  protected TakePointCardButton takePointButton;
  private final double BUTTONS_X_SCALE = 0.92;
  private final double BUTTONS_Y_SCALE = 0.19;
  private final double DONE_X_OFFSET = 0.0;
  private final double DONE_Y_OFFSET = 0.05;
  private final double PASS_X_OFFSET = 0.0;
  private final double PASS_Y_OFFSET = -0.05;
  private final double TAKE_X_SCALE = 0.5;
  private final double TAKE_Y_SCALE = 0.6;
  private final double TAKE_X_MESSAGE_OFFSET = 0.0;
  private final double TAKE_Y_MESSAGE_OFFSET = 0.05;
  protected final double MESSAGE_X_OFFSET_SCALE = -0.14;
  protected final double MESSAGE_Y_OFFSET_SCALE = 0;
  protected int[] buttonsPosition;
  protected int[] doneButtonPosition;
  protected int[] passButtonPosition;
  protected int[] doneAndPassMessagePosition;
  protected int[] takePointButtonPosition;
  protected int[] takePointMessagePostion;

  protected Deck[] middle;
  private double MIDDLE_POSITION_X_SCALE = 0.235;
  private final double MIDDLE_POSITION_Y_SCALE = 0.2;
  private final double MIDDLE_SEPERATION_SCALE = 0.065;
  private final double MIDDLE_GAP_COUNT = 0.11;
  private final double Y_SEPERATION_SCALE = 0.05;
  private final double MOUSE_VARIANCE_SCALE = 0.5;
  private int middleX;
  private int middleY;
  protected int middleSeperation;
  protected int middle2X;
  protected int jokersX;
  protected int playableYPosition;
  protected int playedYPosition;
  protected int hiddenPlayedYPosition;
  protected int ySeperation;
  private int mouseVariance;

  private PlayerList players;
  private String[] playerNames;
  protected final int NUM_PLAYERS = 2;
  private final double LARGE_TEXT_SCALE = 0.065;
  private final double MEDIUM_TEXT_SCALE = 0.05;
  private final double SMALL_TEXT_SCALE = 0.03;
  private final double TINY_TEXT_SCALE = 0.017;
  protected int largeTextSize;
  protected int mediumTextSize;
  protected int smallTextSize;
  protected int tinyTextSize;
  protected final static int HAND_SIZE = 5;
  protected int[] currentPlayerPosition;
  protected int[] currentScorePosition;
  protected int[] hiddenPlayerPosition;
  protected int[] hiddenPlayerScorePosition;
  protected final double WINNER_NAME_X_OFFSET = 0;
  protected final double WINNER_NAME_Y_OFFSET = -0.17;
  protected final double END_SCORE_Y_SCALE = 0.85;
  protected final double END_SCORE_X_SCALE = 0.15;
  protected final double END_SCORE_X_SPACER_SCALE = 0.70;
  protected final double END_SCORE_NAME_Y_SPACER_SCALE = 0.05;
  protected int endScoreY;
  protected int endScoreX;
  protected int endScoreXSpacer;
  protected int endScoreNameYSpacer;

  private DisplayScreen screen; // 0 = shows round, 1 = shows current player, 2 = shows player
                                // screen
  private DisplayScreen prevScreen;

  protected boolean arsenalSwap;
  protected boolean suddenDeath;
  private int roundCount;

  protected int jokersFound;
  private ArrayList<Card> jokersDisplayed;

  private ArrayList<Object[]> addedTo14;
  protected ArrayList<Card> opponentArsenal;
  protected Object[] theifInfo;
  protected Player theif;
  protected Player opponentOfTheif;
  private final double OPPONENT_ARSENAL_Y_SCALE = 0.3;
  private int[] opponentArsenalPosition;
  private Card selectedCardToSteal;

  protected final double ARSENAL_SELECTED_STROKE_SCALE = 0.003;
  protected int selectedStrokeWeight;
  protected final int STROKE_RADIUS = 8;
  protected final int[] ARSENAL_SELECTED_STROKE_COLOR = new int[] {255, 200, 0};
  protected final double EMPTY_STROKE_SCALE = 0.002;
  protected final int[] EMPTY_STROKE_COLOR = new int[] {95, 95, 95};
  protected final double EMPTY_X_STROKE_SCALE = 0.0015;
  protected final int[] EMPTY_X_STROKE_COLOR = new int[] {95, 95, 95};


  private final double SPLAY_SEPERATION_SCALE = 0.25;
  protected int splaySeperation;



  /**
   * CallBack method Defines initial environment properties such as screen size and to load
   * background images and fonts as the program starts Initializes the backgroundImage and listGUI
   * instance fields.
   */
  @Override
  public void setup() {
    this.fill(0, 0, 0);
    this.getSurface().setTitle("Arsenal - The Card Game"); // Displays text in the title of the
                                                           // display window
    this.getSurface().setResizable(true);
    this.textAlign(PApplet.CENTER, PApplet.CENTER); // Sets the current alignment for drawing text
                                                    // to CENTER
    this.imageMode(PApplet.CENTER); // Sets the location from which images are drawn to CENTER
    this.rectMode(PApplet.CORNERS); // Sets the location from which rectangles are drawn.
    // rectMode(CORNERS) interprets the first two parameters of rect() method as the location of one
    // corner, and the third and fourth parameters as the location of the opposite corner.
    // rect() method draws a rectangle to the display window
    this.focused = true; // Confirms that our Processing program is "focused," meaning that
    // it is active and will accept mouse or keyboard input.
    backgroundImage = this.loadImage("images/greenbackdrop.jpg"); // load the background image
    double bgiScale;
    if (this.height - backgroundImage.height > this.width - backgroundImage.width) {
      bgiScale = this.height / (double) backgroundImage.height;
    } else {
      bgiScale = this.width / (double) backgroundImage.width;
    }


    this.roundCount = 1;
    this.screen = DisplayScreen.ROUND;
    this.prevScreen = DisplayScreen.WINNER;
    this.arsenalSwap = false;
    this.suddenDeath = false;

    backgroundImage.resize((int) (this.displayWidth * bgiScale),
        (int) (this.displayHeight * bgiScale));
    this.doneImage = this.loadImage("images/done.png");
    this.passImage = this.loadImage("images/pass.png");
    this.takeImage = this.loadImage("images/honor_heart-14.png");
    this.cardBack = this.loadImage(this.CARD_BACK);
    this.cardImageHeight = (int) (this.height * this.CARD_IMAGE_SCALE);
    this.doneImageScale = this.height * this.DONE_IMAGE_SCALE;
    this.passImageScale = this.height * this.PASS_IMAGE_SCALE;
    this.takeImageScale = this.height * this.TAKE_IMAGE_SCALE;
    scaleCard(cardBack);
    scaleImage(doneImage, this.doneImageScale);
    scaleImage(passImage, this.passImageScale);
    scaleImage(takeImage, this.takeImageScale);


    this.middleSeperation = (int) (this.width * this.MIDDLE_SEPERATION_SCALE);
    this.middleX = (int) (this.width * this.MIDDLE_POSITION_X_SCALE);
    this.middleY = (int) (this.height * this.MIDDLE_POSITION_Y_SCALE);
    this.middle2X = (int) (middleX + 3 * middleSeperation + MIDDLE_GAP_COUNT * middleSeperation);
    this.jokersX = (int) (middle2X + 4 * middleSeperation + MIDDLE_GAP_COUNT * middleSeperation);
    this.mouseVariance = (int) (this.cardBack.width * MOUSE_VARIANCE_SCALE);
    this.playableYPosition = (int) (middleY + 0.4 * middleSeperation);
    this.ySeperation = (int) (this.height * this.Y_SEPERATION_SCALE);
    this.playedYPosition = (int) (this.playableYPosition + 2.6 * this.ySeperation);
    this.hiddenPlayedYPosition = this.middleY - (this.playedYPosition - this.middleY);

    this.selectedStrokeWeight = (int) (this.ARSENAL_SELECTED_STROKE_SCALE * this.width);

    this.splaySeperation = (int) (this.SPLAY_SEPERATION_SCALE * this.middleSeperation);


    middle = new Deck[5];
    middle[0] = new Deck(this, "10-K", middleX + 0 * middleSeperation, middleY, 10, 13, true);
    middle[1] = new Deck(this, "6-9", middleX + 1 * middleSeperation, middleY, 6, 9, false);
    middle[2] = new Deck(this, "A-5", middleX + 2 * middleSeperation, middleY, 1, 5, false);
    middle[3] = new MiddleCards(this, "Middle2", middle2X, middleY);
    middle[4] = new MiddleCards(this, "Middle1", middle2X + 2 * middleSeperation, middleY);



    this.buttonsPosition =
        new int[] {(int) (this.width * BUTTONS_X_SCALE), (int) (this.height * BUTTONS_Y_SCALE)};

    this.doneButtonPosition =
        new int[] {this.buttonsPosition[0] + (int) (this.width * this.DONE_X_OFFSET),
            this.buttonsPosition[1] + (int) (this.height * this.DONE_Y_OFFSET)};

    this.passButtonPosition =
        new int[] {this.buttonsPosition[0] + (int) (this.width * this.PASS_X_OFFSET),
            this.buttonsPosition[1] + (int) (this.height * this.PASS_Y_OFFSET)};

    this.doneAndPassMessagePosition =
        new int[] {this.buttonsPosition[0] + (int) (this.width * this.MESSAGE_X_OFFSET_SCALE),
            this.buttonsPosition[1] + (int) (this.height * this.MESSAGE_Y_OFFSET_SCALE)};

    this.takePointButtonPosition =
        new int[] {(int) (this.width * this.TAKE_X_SCALE), (int) (this.height * this.TAKE_Y_SCALE)};

    this.takePointMessagePostion = new int[] {
        this.takePointButtonPosition[0] + (int) (this.width * this.TAKE_X_MESSAGE_OFFSET),
        this.takePointButtonPosition[1] + (int) (this.height * this.TAKE_Y_MESSAGE_OFFSET)};

    this.doneButton =
        new DoneButton(this, this.doneImage, this.doneButtonPosition[0], this.doneButtonPosition[1],
            this.doneAndPassMessagePosition[0], this.doneAndPassMessagePosition[1]);

    this.passButton =
        new PassButton(this, this.passImage, this.passButtonPosition[0], this.passButtonPosition[1],
            this.doneAndPassMessagePosition[0], this.doneAndPassMessagePosition[1]);

    this.takePointButton = new TakePointCardButton(this, this.takeImage,
        this.takePointButtonPosition[0], this.takePointButtonPosition[1],
        this.takePointMessagePostion[0], this.takePointMessagePostion[1]);


    this.largeTextSize = ((int) (this.LARGE_TEXT_SCALE * this.width));
    this.mediumTextSize = ((int) (this.MEDIUM_TEXT_SCALE * this.width));
    this.smallTextSize = ((int) (this.SMALL_TEXT_SCALE * this.width));
    this.tinyTextSize = ((int) (this.TINY_TEXT_SCALE * this.width));


    currentPlayerPosition =
        new int[] {((int) (this.middleX - 2 * this.middleSeperation)), this.height / 7 * 5};
    currentScorePosition = new int[] {((int) this.passButtonPosition[0]), this.height};
    hiddenPlayerPosition = new int[] {this.width / 17, this.height / 100};
    hiddenPlayerScorePosition = new int[] {((int) 0), ((int) 0)};
    this.endScoreX = ((int) (this.END_SCORE_X_SCALE * this.width));
    this.endScoreY = ((int) (this.END_SCORE_Y_SCALE * this.height));
    this.endScoreXSpacer = ((int) (this.END_SCORE_X_SPACER_SCALE * this.width));
    this.endScoreNameYSpacer = ((int) (this.END_SCORE_NAME_Y_SPACER_SCALE * this.width));


    jokersFound = 0;
    jokersDisplayed = new ArrayList<>();
    addedTo14 = new ArrayList<>();
    opponentArsenal = null;
    opponentArsenalPosition =
        new int[] {this.width / 2, (int) (this.height * this.OPPONENT_ARSENAL_Y_SCALE)};
    selectedCardToSteal = null;
    this.opponentOfTheif = null;
    this.theifInfo = null;


    playerNames = new String[this.NUM_PLAYERS];
    for (int i = 0; i < this.NUM_PLAYERS; ++i) {
      playerNames[i] = "Player" + (i + 1);
    }
    players = new PlayerList();
    for (int i = 0; i < this.NUM_PLAYERS; ++i) {
      players.add(new Player(this), playerNames[i]);
    }
    players.setInitialPlayer(0);
    players.handReplenish(this, this.middle);
    players.setInitialPlayer(initializeStartingPlayer(this.players));
  }

  /**
   * Sets the size of the application display window
   */
  @Override
  public void settings() {
    size(this.displayWidth - this.displayWidth / 40,
        this.displayHeight - (int) (this.displayHeight / 6.2)); // sets the size of the display
                                                                // window to 800 x 632 pixels
    // size(this.displayWidth,this.displayHeight);
  }


  /**
   * Callback method called in an infinite loop. It draws the Jungle Park's window display
   */
  @Override
  public void draw() {
    this.fill(0, 0, 0);
    this.image(backgroundImage, this.width / 2, this.height / 2); // draw the background image at


    if (middle[0].size() <= 0) {
      updateDisplayScreen(DisplayScreen.WINNER);
    }
    switch (screen) {
      case ROUND:
        displayRound();
        break;
      case PLAYER:
        displayPlayerName();
        break;
      case TURN:
        displayUserTurn();
        break;
      case DECIDE:
        break;
      case STEEL:
        displaySteelSelect();
        break;
      case BADTHEIVES:
        displayBadTheives();
        break;
      case WINNER:
        displayWinner();
        break;
    }


  }

  protected void scaleCard(PImage image) {
    int scale = this.cardImageHeight / image.height;
    image.resize((image.width * scale), (this.cardImageHeight));
  }

  protected void scaleImage(PImage image, double scale) {
    image.resize((int) (image.width * scale), (int) (image.height * scale));
  }

  private int initializeStartingPlayer(PlayerList players) {
    int[] countP1 = players.get(0).sameSuitInHand();
    int[] countP2 = players.get(1).sameSuitInHand();
    int maxP1 = (countP1[0] * 10 + countP1[1]);
    int maxP2 = (countP2[0] * 10 + countP2[1]);
    return ((maxP1 > maxP2) ? 0 : 1);
  }

  protected int playableLocation(int x, int y) {
    double[] stackLengths = players.getCurrentPlayer().getStackLengths();
    if (y > (playableYPosition)) {
      if ((x > (middle2X - mouseVariance)) && (x < (middle2X + mouseVariance))
          && (y < (playedYPosition + (int) (stackLengths[0] * ySeperation))))
        return 0;

      if ((x > (middle2X + middleSeperation - mouseVariance))
          && (x < (middle2X + middleSeperation + mouseVariance))
          && (y < (playedYPosition + (int) (stackLengths[1] * ySeperation))))
        return 1;

      if ((x > (middle2X + 2 * middleSeperation - mouseVariance))
          && (x < (middle2X + 2 * middleSeperation + mouseVariance))
          && (y < (playedYPosition + (int) (stackLengths[2] * ySeperation))))
        return 2;

      if ((x > (middle2X + 3 * middleSeperation - mouseVariance))
          && (x < (middle2X + 3 * middleSeperation + mouseVariance))
          && (y < (playedYPosition + (int) (stackLengths[3] * ySeperation))))
        return 3;

    }
    return -1;
  }


  protected boolean playerDone(boolean alreadyPressed) {
    if (players.getCurrentPlayer().hasPlayed()) {
      updateDisplayScreen(DisplayScreen.PLAYER);
      players.getCurrentPlayer().save();
      players.changeCurrentPlayer();
      return true;
    }
    if (alreadyPressed && !players.getCurrentPlayer().hasPlayed()) {
      playerPassed();
      return true;
    }
    return false;
  }

  protected void playerPassed() {

    players.getCurrentPlayer().setPassed(true);
    players.getCurrentPlayer().save();
    players.changeCurrentPlayer();
    players.hideArsenals();
    updateDisplayScreen(DisplayScreen.PLAYER);


    if (allPlayersPassed()) {
//      System.out.println("allPlayersPassed:\n1...");
      players.resolveRound(this, playerNames[0], playerNames[1]);
      if ((this.suddenDeath && !players.haveSameScore())
          || (this.jokersFound >= 2 && !this.suddenDeath)) {
        updateDisplayScreen(DisplayScreen.WINNER);
        return;
      }
//      System.out.println("2...");
      if (this.someoneHasAddedTo14()) {
        this.passButton.press();
        return;
      } else {
        arsenalSwap = !arsenalSwap;
      }
      setNewRound();
//      System.out.println("3.");
    }
  }

  protected void setNewRound() {

    if (this.screen == DisplayScreen.TURN || this.screen == DisplayScreen.STEEL
        || this.screen == DisplayScreen.WINNER) {
      arsenalSwap = !arsenalSwap;
    }

    ((MiddleCards) middle[3]).replenish();
    ((MiddleCards) middle[4]).replenish();
    this.players.setNoPlayerHasPassed();
    if (this.someoneHasAddedTo14() || arsenalSwap) {
      this.updateDisplayScreen(DisplayScreen.PLAYER);
    } else {
      this.updateDisplayScreen(DisplayScreen.ROUND);
      ++roundCount;
      players.changeStartingPlayer();
      players.setInitialPlayer(players.get(players.getStartingPlayer().getName()));
      players.handReplenish(this, this.middle);
      players.hideArsenals();
    }
  }

  public boolean allPlayersPassed() {
    return (this.NUM_PLAYERS - numPlayersPassed() == 0);
  }

  public int numPlayersPassed() {
    return players.numPlayersPassed();
  }

  public boolean validStacks() {
    return players.getCurrentPlayer().validStacks();
  }

  public int validAddSwap() {
    return players.getCurrentPlayer().validAddSwap();
  }

  public Card middleCardAt(int index) {

    Card middleCard = null;
    int middleIndex = calcMiddleIndex(index);
    if (middleIndex == 4) {
      index -= 2;
    }
    if (index >= ((MiddleCards) this.middle[middleIndex]).size()) {
      throw new ArrayIndexOutOfBoundsException(
          "middle[" + middleIndex + "] does not have an index " + index);
    }
    middleCard = ((MiddleCards) this.middle[middleIndex]).get(index);

    return middleCard;
  }

  public int removeMiddleCardAt(int index, Card cardRemoved) {
    int middleCardIndex;
    index = calcMiddleIndex(index);
    middleCardIndex = ((MiddleCards) this.middle[index]).remove(cardRemoved);
    return middleCardIndex;
  }

  public int numFoundInMiddle(Card card) {
    int containsCount = 0;
    for (int i = 0; i < middle[3].size(); ++i) {
      if (middle[3].cards.get(i).compareTo(card) == 0) {
        containsCount++;
      }
    }
    for (int i = 0; i < middle[4].size(); ++i) {
      if (middle[4].cards.get(i).compareTo(card) == 0) {
        containsCount++;
      }
    }
    return containsCount;
  }

  public void jokerPicked(boolean fromMiddle) {
    Card joker = new Card(this, 0, 'W');
    ++jokersFound;
    if (!fromMiddle) {
      if (numFoundInMiddle(joker) >= 2) {
        throw new IllegalArgumentException("there were more than 2 jokers drawn in the game");
      }
      jokersDisplayed.add(joker);
    }
//    System.out.println("jokerPicked()");
//    System.out.println("jokers found: " + jokersFound);
    if (jokersFound >= 2) {
      updateDisplayScreen(DisplayScreen.WINNER);
      return;
    }
  }

  private void displayJokers() {
    for (int i = 0; i < jokersDisplayed.size(); ++i) {
      this.image(jokersDisplayed.get(i).getImage(), this.jokersX + i * this.splaySeperation,
          this.middleY);
    }
  }

  public void addedTo14(Object[] playerInfo) {
    addedTo14.add(playerInfo);
  }

  public void playerActedOn14(Object[] playerWhoActed) {
    if (!addedTo14.isEmpty() && addedTo14.contains(playerWhoActed)) {
      addedTo14.remove(playerWhoActed);
    } else {
      throw new NullPointerException("Invalid player who acted on a 14");
    }
  }

  private void displayRound() {
    this.fill(0, 0, 0);
    this.textSize(this.largeTextSize);
    this.text("Round " + roundCount, this.width / 2, this.height / 3);
    this.textSize(this.tinyTextSize);
    this.text("(click anywhere to continue)", this.width / 30 * 15, (int) (this.height / 3 * 1.25));
    if (this.mousePressed) {
      DRAGGING.increment();
      if (DRAGGING.get() == 1)
        this.updateDisplayScreen(DisplayScreen.PLAYER);
    }
    if (!this.mousePressed) {
      DRAGGING.set(0);
    }
  }

  private void displayPlayerName() {

    DisplayScreen nextDisplayScreen;

    if (this.someoneHasAddedTo14()) {
      if (this.cardsToSteal() || this.pointCardToSteal() != null) {
        this.textSize(this.mediumTextSize);
        this.text("- Espionage -", this.width / 30 * 15, this.height / 3);
        this.textSize(this.smallTextSize);
        this.text(this.theif.getName() + " gets to steal", this.width / 30 * 15,
            (int) (this.height / 3 * 1.3));
        this.textSize(this.tinyTextSize);
        this.text("(click anywhere to continue)", this.width / 30 * 15,
            (int) (this.height / 3 * 1.5));
        nextDisplayScreen = DisplayScreen.STEEL;
      } else {
        this.textSize(this.smallTextSize);
        this.text(this.theif.getName() + " tried to steal from the pauperized",
            this.width / 30 * 15, (int) (this.height / 3 * 1.3));
        this.textSize(this.tinyTextSize);
        this.text("(click anywhere to continue)", this.width / 30 * 15,
            (int) (this.height / 3 * 1.5));
        nextDisplayScreen = DisplayScreen.PLAYER;
      }
    } else {

      this.textSize(this.mediumTextSize);
      this.text(players.getCurrentPlayer().getName(), this.width / 30 * 15, this.height / 3);

      if (arsenalSwap) {
        this.textSize(this.smallTextSize);
        this.text("- Arsenal Add/Swap Phase -", this.width / 30 * 15,
            (int) (this.height / 3 * 1.3));
        this.textSize(this.tinyTextSize);
        this.text("(click anywhere to continue)", this.width / 30 * 15,
            (int) (this.height / 3 * 1.5));
        nextDisplayScreen = DisplayScreen.TURN;
      } else {
        this.textSize(this.smallTextSize);
        this.text("- Battle Phase -", this.width / 30 * 15, (int) (this.height / 3 * 1.3));
        this.textSize(this.tinyTextSize);
        this.text("(click anywhere to continue)", this.width / 30 * 15,
            (int) (this.height / 3 * 1.5));
        nextDisplayScreen = DisplayScreen.TURN;
      }
    }


    if (this.mousePressed) {
      DRAGGING.increment();
      if (DRAGGING.get() == 1) {
        updateDisplayScreen(nextDisplayScreen);
        // if the screen does not change, then someone tried to steel from the pauperized
        if (screen == DisplayScreen.PLAYER) {
          this.addedTo14.remove(this.theifInfo);
          if (this.someoneHasAddedTo14()) {
            updateWorstTheif();
          } else {
            this.addedTo14.clear();
            this.arsenalSwap = !arsenalSwap;
            this.setNewRound();
          }
        }
      }
    }
    if (!this.mousePressed) {
      DRAGGING.set(0);
    }
  }

  private void displayBadTheives() {
    this.textSize(this.smallTextSize);
    this.text("Theives have met their match", this.width / 30 * 15, (int) (this.height / 3 * 1.3));
    this.textSize(this.tinyTextSize);
    this.text("(click anywhere to continue)", this.width / 30 * 15, (int) (this.height / 3 * 1.5));

    if (this.mousePressed) {
      DRAGGING.increment();
      if (DRAGGING.get() == 1) {
        updateDisplayScreen(DisplayScreen.PLAYER);
        this.addedTo14.clear();
        this.arsenalSwap = !arsenalSwap;
        this.setNewRound();
      }
    }
    if (!this.mousePressed) {
      DRAGGING.set(0);
    }
  }

  private void displayUserTurn() {
    for (int i = 0; i < middle.length; ++i) {
      middle[i].draw();
    }
    players.draw();
    if (!arsenalSwap && (this.numPlayersPassed() < this.NUM_PLAYERS - 1)) {
      doneButton.draw();
    }
    passButton.draw();
    displayJokers();
  }

  private void displaySteelSelect() {

    passButton.draw();
    takePointButton.draw();

    boolean firstClick = false;

    if (this.mousePressed) {
      DRAGGING.increment();
      if (DRAGGING.get() == 1) {
        firstClick = true;
      }
    } else {
      DRAGGING.set(0);
    }

    if (firstClick) {
      Card cardUnderMouse = opponentCardUnderMouse();
      if (cardUnderMouse != null) {
        selectedCardToSteal = cardUnderMouse;
      }
    }
    int x;
    int y = this.opponentArsenalPosition[1];
    PImage image;

    for (int i = 0; i < opponentArsenal.size(); i++) {
      x = this.opponentArsenalPosition[0] + i * this.middleSeperation;
      image = opponentArsenal.get(i).getImage();
      this.image(image, x, y);
      if (theifHasChosen() && opponentArsenal.get(i).compareTo(this.selectedCardToSteal) == 0) {
        this.stroke(this.ARSENAL_SELECTED_STROKE_COLOR[0], this.ARSENAL_SELECTED_STROKE_COLOR[1],
            this.ARSENAL_SELECTED_STROKE_COLOR[2]);
        this.strokeWeight(this.selectedStrokeWeight);
        this.noFill();
        this.rectMode(this.CENTER);
        this.rect(x, y, image.width, image.height, this.STROKE_RADIUS);
        this.noStroke();
      }
    }
  }

  private void displaySuddenDeath() {
    int x = this.width / 2;
    int y = this.height / 3;
    this.textSize(this.mediumTextSize);
    this.text("Sudden Death", x, y);
    this.textSize(this.tinyTextSize);
    this.text("(click anywhere to continue)", this.width / 30 * 15, (int) (this.height / 3 * 1.5));
    if (this.mousePressed) {
      DRAGGING.increment();
      if (DRAGGING.get() == 1) {
        setNewRound();
        suddenDeath = true;
      }
    }
    if (!this.mousePressed) {
      DRAGGING.set(0);
    }
  }

  private void displayWinner() {
    int x = this.width / 2;
    int y = this.height / 3;
    ArrayList<Player> winners;
    winners = players.playersWithHighestScore();
    if (winners.size() > 1) {
//      System.out.println("Scores tied.");
      if (middle[0].size() > 0) {
        displaySuddenDeath();
        return;
      }
      winners = players.mostPowerfulPlayers(winners);
      if (winners.size() > 1) {
//        System.out.println("Powers tied.");
        winners = players.playersWithHighestMenialTotal(winners);
      }
    }
    players.displayPlayerScores(this);
    this.textSize(this.mediumTextSize);
    if (winners.size() > 1) {
      String names = "";
      for (int i = 0; i < winners.size(); ++i) {
        names += winners.get(i).getName();
        names += (i == winners.size() - 1) ? " " : " and ";
      }
      this.text(names + "tied the game!", x, y);
    } else if (winners.size() < 1) {
      this.text("Nobody scored. wow.", x, y);
    } else {
      this.text(winners.get(0).getName() + " won the game!", x, y);
    }
  }

  private Card opponentCardUnderMouse() {
    int x = this.mouseX;
    int y = this.mouseY;
    int width;
    int height;
    int xMin;
    int xMax;
    int yMin;
    int yMax;
    for (int i = 0; i < opponentArsenal.size(); ++i) {
      width = opponentArsenal.get(i).getImage().width;
      height = opponentArsenal.get(i).getImage().height;
      xMin = this.opponentArsenalPosition[0] + i * this.middleSeperation - width / 2;
      xMax = this.opponentArsenalPosition[0] + i * this.middleSeperation + width / 2;
      yMin = this.opponentArsenalPosition[1] - height / 2;
      yMax = this.opponentArsenalPosition[1] + height / 2;
      if (x > xMin && x < xMax && y > yMin && y < yMax) {
        return opponentArsenal.get(i);
      }
    }
    return null;
  }

  public boolean theifHasChosen() {
    return (this.selectedCardToSteal != null);
  }

  public boolean someoneHasAddedTo14() {
    return (this.addedTo14.size() > 0);
  }

  public boolean cardsToSteal() {
    return (((Player) this.addedTo14.get(0)[1]).getArsenal().getCards().size() > 0);
  }

  public void stealCard() {
    this.opponentArsenal.remove(this.selectedCardToSteal);
    this.theif.addToArsenal(selectedCardToSteal);
    this.selectedCardToSteal = null;
    this.addedTo14.remove(theifInfo);
  }

  public Card pointCardToSteal() {
    ArrayList<Card> scoresToSteel = this.opponentOfTheif.getCardsScored();
    Card onePointer = null;
    if (!scoresToSteel.isEmpty()) {
      for (Card scoreCard : scoresToSteel) {
        if (Card.scoreOfCard(scoreCard) == Card.getMenialVal()) {
          onePointer = scoreCard;
        }
      }
    }
    return onePointer;
  }


  public boolean theifTakesPointCard() {
    Card stolenCard = pointCardToSteal();
    if (stolenCard == null) {
      return false;
    }
    this.theif.getCardsScored().add(stolenCard);
    this.opponentOfTheif.getCardsScored().remove(stolenCard);
    this.addedTo14.remove(this.theifInfo);
    return true;
  }


  public void updateWorstTheif() {
    this.theifInfo = this.calcWorstTheif();
    if (theifInfo != null) {
      this.theif = (Player) this.theifInfo[0];
      this.opponentOfTheif = (Player) this.theifInfo[1];
      this.opponentArsenal = this.opponentOfTheif.getArsenal().getCards();
    } else {
      this.updateDisplayScreen(DisplayScreen.BADTHEIVES);
    }
  }


  public Object[] calcWorstTheif() {

    Object[] worstPlayerInfo = null;

    int worseThanCount;
    Object[] playerInfo;
    int middleSuitCount;
    Card maxOfSuit;
    int opponentSameSuitCount;
    Card opponentMaxOfSuit;
    boolean playerHasSuit;
    boolean opponentHasSuit;
    boolean playerHasLeastOfSuit;
    boolean neitherPlayerHasSuit;
    int numTies = 0;

    if (this.addedTo14.size() <= 0) {
      throw new IllegalArgumentException("There are no theifs");
    } else if (this.addedTo14.size() <= 1) {
      return this.addedTo14.get(0);
    } else {
      while (numTies < this.addedTo14.size()) {
        worseThanCount = 0;
        playerInfo = addedTo14.get(0);
        // System.out.println(((Player)(playerInfo[0])).getName());
        middleSuitCount = (Integer) playerInfo[2];
        maxOfSuit = (Card) playerInfo[3];
        playerHasSuit = (middleSuitCount > 0);
        for (int i = 1; i < addedTo14.size(); i++) {
          opponentSameSuitCount = (Integer) addedTo14.get(i)[2];
          opponentMaxOfSuit = (Card) addedTo14.get(i)[3];
          opponentHasSuit = (opponentSameSuitCount > 0);
          neitherPlayerHasSuit = (!opponentHasSuit && !playerHasSuit);
          playerHasLeastOfSuit = middleSuitCount < opponentSameSuitCount;
          if (neitherPlayerHasSuit) {
            numTies++;
          } else {
            if ((playerHasLeastOfSuit)
                || ((playerHasSuit) && (middleSuitCount == opponentSameSuitCount)
                    && (maxOfSuit.getValue() < opponentMaxOfSuit.getValue()))) {
              if (++worseThanCount >= addedTo14.size() - 1) {
                worstPlayerInfo = playerInfo;
                return worstPlayerInfo;
              }
            } else {
              numTies++;
            }
          }
        }
        // System.out.println(((Player)(addedTo14.get(0)[0])).getName());
        addedTo14.remove(playerInfo);
        addedTo14.add(playerInfo);
      }
      // if all tied with no cards of the same suit as the middle
      return null;
    }
  }

  public int calcMiddleIndex(int index) {

    switch (index) {
      case 0:
      case 1:
        return 3;
      case 2:
      case 3:
        return 4;
      default:
        return -1;
    }
  }

  public void updateDisplayScreen(DisplayScreen displayScreen) {
    this.prevScreen = this.screen;
    this.screen = displayScreen;
  }

  public DisplayScreen getDisplayScreen() {
    return this.screen;
  }

  public void setDisplayScreen(DisplayScreen screenToSet) {
    screen = screenToSet;
  }

  public DisplayScreen getPreviousScreen() {
    return this.prevScreen;
  }

  /**
   * This main method starts the application
   * 
   * @param args
   */
  public static void main(String[] args) {
    // starts the application (calls PApplet main() method with the name
    // of the PApplet class to run as parameter)
    PApplet.main("GameBoard");
  }
}

