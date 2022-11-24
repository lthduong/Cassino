# Cassino
# I.	General description:
This is a simple card game revolving around capturing cards in the table using those in your hand. One deck of card has 52 cards, each has a suit and a value. There are overall 4 suits and 13 possible values. The game can be played with other human or computer player. However, for the sake of simplicity of the game, the maximum number of human players is 6 and computer players is 3. More information on the rule:


## 2.1	Rounds:
The deck is shuffled in the beginning of every round and the dealer deals 4 cards to every player (they are not visible to other players) and 4 cards on the table (visible for everyone). The rest of the cards are left on the table upside down.

## 2.2	Actions in each round:
A player can play out one of his/her cards: it can be used either for taking cards from the table or to just putting it on the table. If the player cannot take anything from the table, he/she must put one of his/her cards on the table.

If the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended.

The number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.

Player must draw a new card from the deck after using a card so that he/she has always 4 cards in his/her hand. (When the deck runs out, everyone plays until there are no cards left in any player’s hand).

Player can use a card to take one or more cards of the same value and cards such that their summed value is equal to i * the used card for some integer i.

## 2.3	Sweep:
If some player gets all the cards from the table at the same time, he/she gets a so- called sweep which is written down.

## 2.4	Special cards:
There are a couple of cards that are more valuable in the hand than in the table,

- Aces: 14 in hand, 1 on table
- Diamonds-10: 16 in hand, 10 on table
- Spades-2: 15 in hand, 2 on table

## 2.5	Scoring: 
When every player runs out of cards, the last player to take cards from the table gets the rest of the cards from the table. After this the points are calculated and added to the existing scores.
The following things grant points:

- Each sweep grants 1 point
- Each Ace grants 1 point
- The player with most cards gets 1 point
- The player with most spades gets 2 points
- The player with Diamonds-10 gets 2 points
- The player with Spades-2 gets 1 point


# User interface
When open the program, the user can choose the number of human and computer players for the game and then click “Ok”. 

![Firstscreen](https://user-images.githubusercontent.com/94646688/203861289-badb13c7-e9fc-4f65-85b1-52081498aa29.png)
 
The program will then take the user to the fields to input the player’s names. The computer player’s name will be Cmp1, Cmp2, and Cmp3. If the user wants to change the number of players, they can click “Back”. When everything is set, the user can hit “Confirm” to start the game.

![namescreen](https://user-images.githubusercontent.com/94646688/203861315-677234a0-2ea0-42df-b037-9a15fe1ca857.png)
 
The game screen has 4 sections. The first section is the info section, locates on top of the screen. It has the name of the player of that turn and the number of cards left in the deck.

The second part is the table section, where the images of the table cards are displayed. Maximum of 14 cards can be displayed. If the number of cards exceed 15, the excessive cards will not be displayed. But after some test runs, I did not got into such situation. 

The third section is the hand section, where the cards on the hand are displayed. There are three buttons: Pile, Capture, and Drop. The Pile button used to check the pile cards of the player. The player can choose cards on hand and table and use the buttons Capture or Drop to perform action in a turn. The final section of the game screen is the score section. The human player will be displayed on the first line, and the computer players are on the second line. 

![game1](https://user-images.githubusercontent.com/94646688/203861399-5955337b-5246-41a4-b2b1-74f24cd6df6e.png)

In case the player attempts to perform an invalid action, a dialog will pop up to notice the player.  

![game5](https://user-images.githubusercontent.com/94646688/203861445-a3b4f8e8-7bd5-43ff-bac5-912d4ba3e112.png)
 
When a turn is complete, a screen will show up to announce turn change. User can click on the screen to move on. In case the next player is a computer player, a dialog will pop up to announce the its action
 
![game3](https://user-images.githubusercontent.com/94646688/203861458-440afac0-62b0-4885-8816-b451a970115d.png)

![game4](https://user-images.githubusercontent.com/94646688/203861475-4fac96b0-1609-4d61-8d42-401b6c92779f.png)


When the game is complete, a scoreboard will show up with players who has highest scores colored in red. 

If any player wants to know the rules, they can click the Help menu and then Instruction button, an instruction window will be opened. 

![instruction](https://user-images.githubusercontent.com/94646688/203861496-50558880-c62d-4796-a5a2-fee11f0b3a43.png)
 
The user can save or load file using the Save and Load buttons in the Game menu. When clicked, a file chooser will be opened, where the user can specify the file that they want to use. 
![game6](https://user-images.githubusercontent.com/94646688/203861536-0ee6a08d-6285-4070-bbb2-b1bf8e177e21.png)

