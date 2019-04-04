import java.util.ArrayList;
import java.util.Arrays;

public class PlayerList {

  private PlayerNode head;
  private int size;
  private PlayerNode currentPlayer;
  private PlayerNode startingPlayer;

  public PlayerList() {
    head = null;
    size = 0;
    currentPlayer = null;
    startingPlayer = null;
  }



  public void add(Player playerToAdd) {
    if (head == null) {
      head = new PlayerNode(playerToAdd);
      head.setNext(head);
      size++;
    } else {
      PlayerNode runner = head;
      while (!runner.getNext().equals(head)) {
        runner = runner.getNext();
      }
      runner.setNext(new PlayerNode(playerToAdd));
      runner = runner.getNext();
      runner.setNext(head);
      size++;
    }
  }


  public void add(Player playerToAdd, String name) {
    if (head == null) {
      head = new PlayerNode(playerToAdd);
      head.getPlayer().setName(name);
      head.setNext(head);
      size++;
    } else {
      PlayerNode runner = head;
      while (!runner.getNext().equals(head)) {
        runner = runner.getNext();
      }
      runner.setNext(new PlayerNode(playerToAdd));
      runner = runner.getNext();
      runner.setNext(head);
      runner.getPlayer().setName(name);
      size++;
    }
  }



  public Player get(int index) {
    return playerNodeAt(index).getPlayer();
  }

  public int get(String playerName) {
    PlayerNode runner;
    for (int i = 0; i < this.size; ++i) {
      runner = playerNodeAt(i);
      if (runner != null && runner.getPlayer().getName().equals(playerName)) {
        return i;
      }
    }
    return -1;
  }



  public int size() {
    return this.size;
  }



  public void setInitialPlayer(int playerIndex) {
    startingPlayer = playerNodeAt(playerIndex);
    currentPlayer = playerNodeAt(playerIndex);
  }

  public Player getCurrentPlayer() {
    if (currentPlayer == null) {
      throw new IllegalArgumentException("no current player set or no players added");
    }
    return currentPlayer.getPlayer();
  }

  public Player getStartingPlayer() {
    if (startingPlayer == null) {
      throw new IllegalArgumentException("no starting player set or no players added");
    }
    return startingPlayer.getPlayer();
  }

  public void changeCurrentPlayer() {
    if (currentPlayer == null) {
      throw new IllegalArgumentException("no current player set or no players added");
    }
    currentPlayer = currentPlayer.getNext();
  }

  public void changeStartingPlayer() {
    if (startingPlayer == null) {
      throw new IllegalArgumentException("no starting player set or no players added");
    }
    startingPlayer = startingPlayer.getNext();
  }



  public void handReplenish(GameBoard processing, Deck[] decks) {
    PlayerNode firstPlayerToDraw;
    boolean isLastPlayer = (get(startingPlayer.getPlayer().getName()) == 0);
    firstPlayerToDraw = (isLastPlayer) ? playerNodeAt(this.size - 1)
        : playerNodeAt(get(startingPlayer.getPlayer().getName()) - 1);
    PlayerNode runner = firstPlayerToDraw;
    while (!handsAreFilled() && processing.jokersFound < 2) {
      runner.getPlayer().pick(decks);
      runner = runner.getNext();
    }
  }


  public void draw() {
    int currWasChangedCount = 0;
    for (int i = 0; i < this.size; i++) {
      if (this.currentPlayer.getPlayer().hasPassed()) {
        this.changeCurrentPlayer();
        currWasChangedCount++;
      }
    }

    if (currWasChangedCount == this.size) {
      throw new IllegalArgumentException("cannot draw passed players");
    }
    currentPlayer.getPlayer().displayCurrentPlayer();
    PlayerNode runner = currentPlayer.getNext();
    while (!runner.equals(currentPlayer)) {
      runner.getPlayer().displayHiddenPlayer();
      runner = runner.getNext();
    }
  }



  private PlayerNode playerNodeAt(int index) {
    PlayerNode runner = head;
    if (index >= size || index < 0) {
      throw new IllegalArgumentException("invalid player index");
    }
    if (index == 0 && head != null) {
      return head;
    }
    for (int i = 0; i < index; ++i) {
      runner = runner.getNext();
    }
    return runner;
  }



  private boolean handsAreFilled() {
    PlayerNode runner = head;
    for (int i = 0; i < size; i++) {
      if (runner.getPlayer().cardsCount() < GameBoard.HAND_SIZE) {
        return false;
      }
      runner = runner.getNext();
    }
    return true;
  }


  public void resolveRound(GameBoard processing, String name1, String name2) {

    boolean[] middleRemoved = new boolean[4];
    Arrays.fill(middleRemoved, false);
    Player p1 = get(get(name1));
    Player p2 = get(get(name2));
    Object[] p1Info;
    Object[] p2Info;
    ArrayList<Card> p1Stack;
    ArrayList<Card> p2Stack;
    int p1StackTotal;
    int p2StackTotal;
    int[] p1SameSuits;
    int[] p2SameSuits;
    int p1SuitCount;
    int p2SuitCount;
    Card middleCard;
    Card p1MaxOfSuit;
    Card p2MaxOfSuit;
    Player winnerOfAction;
    boolean someoneHasSuit;
    boolean bothHaveSuit;


    for (int i = 0; i < 4; i++) {
      winnerOfAction = null;
      if ((p1.getPlayed()[i][0] == null) && (p2.getPlayed()[i][0] == null)) {
        continue;
      }
      middleCard = processing.middleCardAt(i);
      p1Info = playerStackInfo(p1, middleCard, i);
      p2Info = playerStackInfo(p2, middleCard, i);

      // System.out.println("p1Info: " + Arrays.toString(p1Info));
      // System.out.println("p2Info: " + Arrays.toString(p2Info));

      p1Stack = (ArrayList<Card>) p1Info[0];
      p2Stack = (ArrayList<Card>) p2Info[0];
      p1MaxOfSuit = (Card) p1Info[3];
      p2MaxOfSuit = (Card) p2Info[3];
      p1StackTotal = (Integer) p1Info[4];
      p2StackTotal = (Integer) p2Info[4];
      p1SuitCount = (Integer) p1Info[5];
      p2SuitCount = (Integer) p2Info[5];
      // p1SameSuits = p1.suitsCount(p1Stack);
      // p2SameSuits = p2.suitsCount(p2Stack);

      // System.out.print("p1SameSuits: ");
      // for (int j = 0; j < p1SameSuits.length; ++j) {
      // System.out.print(p1SameSuits[j] + " , ");
      // }
      // System.out.print("\np2SameSuits: ");
      // for (int j = 0; j < p2SameSuits.length; ++j) {
      // System.out.print(p2SameSuits[j] + " , ");
      // }

      // System.out.println("\nmiddleSuit: " + middleSuit);
      someoneHasSuit = (p1MaxOfSuit == null && p2MaxOfSuit == null) ? false : true;
      bothHaveSuit = (p1MaxOfSuit != null && p2MaxOfSuit != null) ? true : false;

      // System.out.println(p1SuitCount + " " + p2SuitCount);
      // if adding to the same number
      if (p1StackTotal == p2StackTotal) {
        // if no one has the middle suit stack cleared and middle card removed
        if (!someoneHasSuit) {
          middleRemoved[i] = true;
          continue;
        }
        // if both have the same number of cards with the same suit as the middle card
        if (p1SuitCount == p2SuitCount) {
          // if both have the same suit as the middle
          if (bothHaveSuit) {
            // if one has higher of the suit that player wins stack
            if (p1MaxOfSuit.getValue() > p2MaxOfSuit.getValue()) {
              winnerOfAction = p1;
            } else if (p1MaxOfSuit.getValue() < p2MaxOfSuit.getValue()) {
              winnerOfAction = p2;
            } else {
              throw new IllegalStateException("two of the same cards found in stack " + (i + 1));
            }
          } else {
            // if both do not have the middle suit
            middleRemoved[i] = true;
            continue;
          }
        } else if (p1MaxOfSuit != null) {
          winnerOfAction = p1;
        } else if (p2MaxOfSuit != null) {
          winnerOfAction = p2;
        } else {
          throw new IllegalStateException("two of the same cards found in stack " + (i + 1));
        }
        // if not adding to the same number the player adding to highest number wins stack
      } else if (p1StackTotal > p2StackTotal) {
        winnerOfAction = p1;
        middleRemoved[i] = true;
      } else {
        winnerOfAction = p2;
        middleRemoved[i] = true;
      }

      // if a winner was found,
      if (winnerOfAction != null) {
        if (winnerOfAction.equals(p1)) {
          handleWinnings(processing, p1, p1Info);
        }
        if (winnerOfAction.equals(p2)) {
          handleWinnings(processing, p2, p2Info);
        }
      }
    }



    ArrayList<Card> middleCards = new ArrayList<>();
    for (int i = 0; i < middleRemoved.length; i++) {
      middleCards.add(processing.middleCardAt(i));
    }
    for (int i = 0; i < middleCards.size(); ++i) {
      if (middleRemoved[i] == true) {
        processing.removeMiddleCardAt(i, middleCards.get(i));
      }
    }
    p1.clear2DCardArray(p1.getPlayed());
    p2.clear2DCardArray(p2.getPlayed());
  }

  public boolean haveSameScore() {
    PlayerNode runner = head;
    int firstScore = runner.getPlayer().calculateScore();
    runner = runner.getNext();
    for (int i = 0; i < this.size - 1; ++i) {
      if (runner.getPlayer().calculateScore() != firstScore) {
        return false;
      }
      runner = runner.getNext();
    }
    return true;
  }


  // returns Object[]{ArrayList<Card> stack, Card min,Card max,int total}
  private Object[] playerStackInfo(Player player, Card middleCard, int index) {
    ArrayList<Card> p1Stack = new ArrayList<>();
    Integer p1StackTotal = 0;
    Card p1MaxCard = null;
    Card p1MinCard = null;
    Card p1MaxOfSuit = null;
    int[] p1SameSuits = new int[4];
    int middleSuit;
    int p1SuitCount;
    boolean isMiddleSuit = false;
    boolean isMiddleCard = false;

    addCardsToList(p1Stack, player.getPlayed()[index]);
    char middleSuitChar = middleCard.getSuit();
    p1SameSuits = player.suitsCount(p1Stack);
    middleSuit = middleCard.suitIntValue(middleCard.getSuit());
    p1SuitCount = p1SameSuits[middleSuit];
    p1Stack.add(middleCard);

    for (int i = 0; i < p1Stack.size(); ++i) {
      isMiddleCard = (p1Stack.get(i).compareTo(middleCard) == 0) ? true : false;
      // System.out.println("isMiddleCard: " + isMiddleCard);
      isMiddleSuit = (p1Stack.get(i).getSuit() == middleSuitChar);
      // System.out.println("isMiddleSuit: " + isMiddleSuit);
      if (i == 0) {
        p1MaxCard = p1Stack.get(i);
        p1MinCard = p1Stack.get(i);
      }


      if (p1MaxOfSuit == null) {
        if (isMiddleSuit && !isMiddleCard) {
          p1MaxOfSuit = p1Stack.get(i);
          // System.out.println("1");
        }
      } else if (isMiddleSuit && p1Stack.get(i).compareTo(p1MaxOfSuit) > 0 && !isMiddleCard) {
        p1MaxOfSuit = p1Stack.get(i);
        // System.out.println("2");
      }


      if (p1Stack.get(i).compareTo(p1MaxCard) > 0)

      {
        p1MaxCard = p1Stack.get(i);
      }
      if (p1Stack.get(i).compareTo(p1MinCard) < 0) {
        p1MinCard = p1Stack.get(i);
      }
      p1StackTotal += p1Stack.get(i).getValue();
    }
    return new Object[] {p1Stack, p1MinCard, p1MaxCard, p1MaxOfSuit, p1StackTotal, p1SuitCount};
  }


  private void handleWinnings(GameBoard processing, Player winner, Object[] playerInfo) {

    int playerStackTotal = (Integer) playerInfo[4];

    switch (playerStackTotal) {
      case 5:
        winner.addToArsenal((Card) playerInfo[2]);
        break;
      case 10:
        winner.addToArsenal((Card) playerInfo[1]);
        break;
      case 14:
        Player nextPlayer = playerNodeAt(this.get(winner.getName())).getNext().getPlayer();
        Object[] modifiedInfo = new Object[4];
        modifiedInfo[0] = winner;
        modifiedInfo[1] = nextPlayer;
        modifiedInfo[2] = playerInfo[5];
        modifiedInfo[3] = playerInfo[3];
        processing.addedTo14(modifiedInfo);
        break;
      case 15:
        winner.scored((Card) playerInfo[2]);
        break;
      case 20:
        winner.scored((Card) playerInfo[1]);
        break;
    }
  }


  private void addCardsToList(ArrayList<Card> cards, Card[] cardsToAdd) {
    for (int i = 0; i < cardsToAdd.length; ++i) {
      if (cardsToAdd[i] != null) {
        cards.add(cardsToAdd[i]);
      }
    }
  }

  public void hideArsenals() {
    PlayerNode runner = head;
    for (int i = 0; i < this.size; ++i) {
      runner.getPlayer().getArsenal().hide();
      runner = runner.getNext();
    }
  }

  public int numPlayersPassed() {
    int count = 0;
    PlayerNode runner = head;
    for (int i = 0; i < this.size; i++) {
      if (runner.getPlayer().hasPassed()) {
        ++count;
      }
      runner = runner.getNext();
    }
    return count;
  }

  public int setNoPlayerHasPassed() {
    int count = 0;
    PlayerNode runner = head;
    for (int i = 0; i < this.size; i++) {
      runner.getPlayer().setPassed(false);
      runner = runner.getNext();
    }
    return count;
  }

  public void displayPlayerScores(GameBoard processing) {
    PlayerNode runner = this.head;
    int x;
    int y;
    int nameY;
    for (int i = 0; i < this.size; i++) {
      x = processing.endScoreX + i * processing.endScoreXSpacer;
      y = processing.endScoreY;
      nameY = runner.getPlayer().displayScore(x, y, true) - processing.cardBack.height / 2
          - processing.endScoreNameYSpacer;
      processing.textSize(processing.smallTextSize);
      processing.text(runner.getPlayer().getName(), x, nameY);
      runner = runner.getNext();
    }
  }

  public ArrayList<Player> playersWithHighestScore() {
    PlayerNode runner = this.head;
    int runnerScore;
    int maxScore = 0;
    ArrayList<Player> playersWithMax = new ArrayList<>();

    for (int i = 0; i < this.size; i++) {
      runnerScore = runner.getPlayer().calculateScore();
      if (runnerScore > maxScore) {
        playersWithMax.clear();
        playersWithMax.add(runner.getPlayer());
        maxScore = runnerScore;
      } else if (runnerScore == maxScore) {
        playersWithMax.add(runner.getPlayer());
      }
      runner = runner.getNext();
    }
    return playersWithMax;
  }

  public ArrayList<Player> mostPowerfulPlayers(ArrayList<Player> inCompetition) {
    ArrayList<Player> playersWithHighestPower = new ArrayList<>();
    int runnerPowerCount = 0;
    int maxPowerCount = 0;

    for (int i = 0; i < inCompetition.size(); ++i) {
      runnerPowerCount = Deck.numPowerCards(inCompetition.get(i).getCardsScored());
      if (runnerPowerCount > maxPowerCount) {
        playersWithHighestPower.clear();
        playersWithHighestPower.add(inCompetition.get(i));
        maxPowerCount = runnerPowerCount;
      } else if(runnerPowerCount == maxPowerCount) {
        playersWithHighestPower.add(inCompetition.get(i));
      }
    }
    if (playersWithHighestPower.size() > 0) {
      return playersWithHighestPower;
    } else {
      return inCompetition;
    }
  }


  public ArrayList<Player> playersWithHighestMenialTotal(ArrayList<Player> inCompetition) {
    ArrayList<Player> playersWithHighestMenialTotal = new ArrayList<>();
    int runnerMenialTotal;
    int maxMenialTotal = 0;

    for (int i = 0; i < inCompetition.size(); ++i) {
      runnerMenialTotal = Deck.totalFaceValueMenialCards(inCompetition.get(i).getCardsScored());
      if (runnerMenialTotal > maxMenialTotal) {
        playersWithHighestMenialTotal.clear();
        playersWithHighestMenialTotal.add(inCompetition.get(i));
        maxMenialTotal = runnerMenialTotal;
      } else if(runnerMenialTotal == maxMenialTotal) {
        playersWithHighestMenialTotal.add(inCompetition.get(i));
      }
    }
    if (playersWithHighestMenialTotal.size() > 0) {
      return playersWithHighestMenialTotal;
    } else {
      return inCompetition;
    }
  }
}
