# FirstProject_Arsenal
"At its root, I started this project for the purpose of gaining programming experience. I only had one semester of coding experience under my belt and, thus, the spagetti code is quite messy with little to no commenting. That being said, writing thousands of lines of code can teach things which just cannot be learned in a classroom. If I were to do it again, I would do a lot more planning and I would not waste time using the PApplet API (I had no idea javaFX existed at the time). I'm not even sure if the game is completely debugged since it has been a while since I worked on it, but, hopefully if you are reading this, you can enjoy the game as much as I enjoyed creating it. Below are the game play instructions."




Arsenal
(The Card Game)

Created by Noah Holmbeck



Setup:
- Three draw piles are shuffled and placed face down in descending order according to their contents.
  1.) A - 5
  2.) 6 - 9
  3.) 10 - K + Jokers
- Two cards are taken from the second lowest pile and separately flipped face up closest to the A - 5 draw pile
- Two cards are taken from the lowest draw pile and separately flipped face up, continuing the pattern on the table the other two cards created (4 consecutive cards face up)
- Players take turns drawing from the A - 5 draw pile till each hand contains 5 cards


Phase One:	 Middle is replenished
- Replenish highest two middle cards with the second lowest available draw pile
- Replenish lowest two middle cards with lowest available draw pile
    - only missing middle cards are replenished for the next round


Phase Two: Players hands are replenished
- Players can choose to draw and/or swap any number of cards from their arsenal
- Starting with the player who was first the round before, alternate drawing one card from the lowest possible pile till both players hand size is equal to five cards
    - the number of cards in an arsenal cannot increase during this phase


Phase Three: Battle
- Starting with the player who has the most (number of cards) of any given suit (and then alternating who starts each round) players dual by laying any number of cards (displaying for their opponent how many by unevenly stacking them) face down in front of the face up cards in the middle. The cards laid must either match the value of the corresponding middle card or they must total to 5, 10, 14, 15, or 20 (corresponding middle card included in the addends). When an equal value card is layed, both cards are washed (discarded). If the face down cards and corresponding middle card total to...
  +5	=> place the highest value card of the addends in the player’s arsenal
  +10	=> place the lowest  value card of the addends in the player’s arsenal
  +14	=> the player steals from their opponent
          - Either a single point score card (if they have any)
          - Or an arsenal card (player may look through opponents entire arsenal hand before selecting)
  +15	=> score the highest value card of the addends
  +20	=> score the lowest value card of the addends
- Players may pass only to stop playing cards from their hand for the rest of the round
- When both players pass (or no cards remain in any player’s hand) all cards on the playing field are turned face up. Each middle card can only give one action each round and players determine who wins the one allowed action on any given middle card by determining who has used the card in a more powerful way. 
    - Adding to 20 > 15 > 14 > 10 > 5 > same value … if a tie … 
    - The greater number of face down cards with the same suit as the corresponding middle card, the greater the strength of the sum … if a tie … 
    - The highest value card of the same suit as the corresponding middle card takes the action.
    * if both players have added to the same value using the same middle card but neither played a card of the same suit as that of the middle, all cards corresponding to (and including) the middle card at hand are discarded and no action is taken by either player.
    * +14 are always dealt with after all other cards and if more than one middle card is successfully used for +14, the stronger (same value system as previously described) of the +14 takes the espionage action last
    * during the dual, players may not remove cards already in play, but they may add additional cards atop of them (given the move still fits within the legal boundaries)


Phase Four: Wash (discard)
- After all appropriate cards are scored or placed in arsenals, remaining face up cards (not including unused middle cards) are washed (discarded) to be replenished the next round.


Victory:
- Gameplay ends when both jokers have been drawn from the highest value draw pile.
- The player with the greatest number of accumulated points wins
    *) 10 - K		=	3 pts
    *) Other 		=	1 pt
* if a Joker is drawn in Phase One, it is played face up in the correct middle slot and cannot be utilized for the remainder of the game. If drawn during Phase Two, the player who drew the card must reveal it, set it to the side face up to show one more joker has leaked from the draw pile, and then replace the card immediately.
* in case of a tie, the first player to score the next point wins. If draw piles empty and the tie remains, the player with the highest total value of three pointers (K - 13, Q - 12, J - 11, 10 - 10) wins. If still a tie, highest totalled face value of one pointers wins. * In case of a tie remaining, the players are equally matched and are bound as friends for life.

