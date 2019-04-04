
public class PlayerNode {

  private PlayerNode next;
  private Player player;
  
  public PlayerNode(Player player) {
    this.player = player;
    this.next = null;
  }
  
  public PlayerNode getNext() {
    return next;
  }
  
  public void setNext(PlayerNode next) {
    this.next = next;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  @Override
  public String toString() {
    return player.getName();
  }
}
