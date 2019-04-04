import java.util.ArrayList;

public class MiddleCards extends Deck {

  private Deck lowest;
  private Deck secondLowest;
  private static boolean emptyPiles;
  private static int numberOfJokers;
  private Card joker;

  public MiddleCards(GameBoard processing, String deckName, int xPosition, int yPosition) {
    super(processing, deckName, xPosition, yPosition, new ArrayList<Card>());
    emptyPiles = false;
    numberOfJokers = 0;
    joker = new Card(processing, 0, 'W');

    for (int i = 2; i >= 0; ++i) {
      if (processing.middle[i].cards().size() > 0) {
        lowest = processing.middle[i];
        secondLowest = (i > 0) ? processing.middle[i - 1] : lowest;
        break;
      }
    }
    replenish();
  }



  public void replenish() {
    int numOriginalJokers = numberOfJokers;
    int jokerNumChange;
    switch (this.deckName) {
      case "Middle1":
        drawLowestCards();
        break;
      case "Middle2":
        drawSecondLowestCards();
        break;
    }
    for (int i = 0; i < cards().size(); ++i) {
      this.processing.scaleCard(cards().get(i).getImage());
    }
    jokerNumChange = numberOfJokers - numOriginalJokers;
    for (int i = 0; i < jokerNumChange; ++i) {
      processing.jokerPicked(true);
    }
  }

  private boolean drawLowestCards() {
    while (cards().size() < 2 && (!emptyPiles)) {
//      System.out.println("lowestLoop - " + emptyPiles);
      Card picked;
      for (int i = 0; i < 3; ++i) {
        if (lowest.cards().size() <= 0) {
          if (i == 2) {
            emptyPiles = true;
//            System.out.println("lowestLoop - " + emptyPiles);
          } else {
            recalibrate(lowest);
          }
        } else {
          picked = lowest.pick();
          cards().add(picked);
          if (picked.compareTo(joker) == 0) {
            numberOfJokers++;
          }
          break;
        }
      }
    }
    // System.out.println(lowest.toString());
    return !emptyPiles;
  }

  private boolean drawSecondLowestCards() {
    while (cards().size() < 2 && (!emptyPiles)) {
//      System.out.println("secondLowestLoop - " + emptyPiles);
      Card picked;
      for (int i = 0; i < 2; ++i) {
        if (secondLowest.cards().size() <= 0) {
          if (i == 1) {
            emptyPiles = true;
//            System.out.println("secondLowestLoop - " + emptyPiles);
          } else {
            recalibrate(secondLowest);
          }
        } else {
          picked = secondLowest.pick();
          cards().add(picked);
          if (picked.compareTo(joker) == 0) {
            numberOfJokers++;
          }
          break;
        }
      }
    }
    // System.out.println(secondLowest.toString());
    return !emptyPiles;
  }



  private void recalibrate(Deck deck) {
    for (int i = 2; i > 0; --i) {
      if (processing.middle[i].equals(deck)) {
        if (deck.equals(lowest)) {
          lowest = processing.middle[i - 1];
        } else if (deck.equals(secondLowest)) {
          secondLowest = processing.middle[i - 1];
        }
        break;
      }
    }
  }

  @Override
  public void draw() {
    for (int i = 0; i < cards().size(); ++i) {
      this.processing.image(cards().get(i).getImage(),
          position[0] + i * processing.middleSeperation, position[1]);
    }
  }

  public Card get(int index) {
    if (index >= cards().size()) {
      throw new IndexOutOfBoundsException("no middleCard at index " + index);
    }
    return cards().get(index);
  }

  public Card remove(int index) {
    if (index >= this.cards().size()) {
      throw new IndexOutOfBoundsException("invalid index for MiddleCards");
    }
    return this.cards().remove(index);
  }

  public int remove(Card otherCard) {
    for (int i = 0; i < cards().size(); i++) {
      if (cards().get(i).compareTo(otherCard) == 0) {
        this.cards().remove(i);
        return i;
      }
    }
    return -1;
  }

  public void empty() {
    this.cards().clear();
  }

}

