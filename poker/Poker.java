package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class Poker {
   public static Dealer dealer;
   public static final int PRE_FLOP = 0;
   public static final int FLOP = 1;
   public static final int TURN = 2;
   public static final int RIVER = 3;

   public static Player playerInput(Player p) {
      Player player = p;
      Scanner scan = new Scanner(System.in);
      String playerAction = scan.next();

      switch (playerAction) {
      case "b":
         dealer.currentBet = scan.nextInt();
         player.bet(dealer.currentBet);
         dealer.pot += dealer.currentBet;
         break;
      case "c":
         player.bet(dealer.currentBet);
         dealer.pot += dealer.currentBet;
         break;
      case "f":
         player.inHand = false;
         dealer.playersInHand--;
         System.out.println("Player " + player.id + " folds");
         break;
      default:
         break;
      }

      return player;
   }

   public static void main(String[] args) {
      dealer = new Dealer();

      int gameState = 0;
      boolean playGame = true;
      int playerId = 0;

      int startingChips = 10000;
      ArrayList<Player> players = new ArrayList<Player>();
      players.add(new Player(playerId++, startingChips));
      players.add(new Bot(playerId++, startingChips));
      players.add(new Bot(playerId++, startingChips));

      while (playGame) {
         switch (gameState) {
         case PRE_FLOP:
            dealer.playersInHand = dealer.getPlayersInHand(players);

            players.get(dealer.dealerButtonPosition % dealer.playersInHand).dealerButton = true;
            if (dealer.playersInHand == 2) {
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).smallBlind = true;
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).position = 2;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).bigBlind = true;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).position = 1;
            } else {
               players.get(dealer.smallBlindPosition % dealer.playersInHand).smallBlind = true;
               players.get(dealer.smallBlindPosition % dealer.playersInHand).position = 1;
               players.get(dealer.bigBlindPosition % dealer.playersInHand).bigBlind = true;
               players.get(dealer.bigBlindPosition % dealer.playersInHand).position = 2;

               for (int i = 2; i < dealer.playersInHand; i++) {
                  players.get((dealer.smallBlindPosition + i) % dealer.playersInHand).position = i + 1;
               }
            }

            for (Player player : players) {
               if (player.bigBlind) {
                  player.blind(dealer.bigBlindAmount);
                  dealer.pot += dealer.bigBlindAmount;
               } else if (player.smallBlind) {
                  player.blind(dealer.smallBlindAmount);
                  dealer.pot += dealer.smallBlindAmount;
               }
            }

            players = dealer.dealHoleCards(players);

            for (Player player : players) {
               if (player.getClass() == Bot.class) {
                  ((Bot) player).evalHoleCards();
               }

               System.out.println("Player " + player.id);
               player.holeCards.printHoleCards();
            }

            // TODO need bet period function. players to global vars?
            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               for (int i = 1; i < players.size(); i++) {
                  players.get(i).bet(dealer.currentBet);
                  dealer.pot += dealer.currentBet;
               }
            }
            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               for (int i = 1; i < players.size(); i++) {
                  players.get(i).bet(dealer.currentBet);
                  dealer.pot += dealer.currentBet;
               }
            }
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               for (int i = 1; i < players.size(); i++) {
                  players.get(i).bet(dealer.currentBet);
                  dealer.pot += dealer.currentBet;
               }
            }
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               for (int i = 1; i < players.size(); i++) {
                  players.get(i).bet(dealer.currentBet);
                  dealer.pot += dealer.currentBet;
               }
            }
            break;
         }

         dealer.currentBet = 0;
         gameState += 1;
         System.out.println("Current Pot: " + dealer.pot);

         if (gameState > RIVER || dealer.playersInHand == 1) {
            if (dealer.playersInHand == 1) {
               for (Player player : players) {
                  if (player.inHand) {
                     System.out.println("Player " + player.id + " wins " + dealer.pot);
                     player.stack += dealer.pot;
                  }
               }
            }

            for (Player player : players) {
               if (player.stack == 0) {
                  players.remove(player);
               }
            }

            System.out.println("\n\n");
            gameState = PRE_FLOP;
            dealer.newHand();

            for (Player player : players) {
               player.position = 0;
               player.bigBlind = false;
               player.dealerButton = false;
               player.smallBlind = false;
            }
         }
      }
   }
}
