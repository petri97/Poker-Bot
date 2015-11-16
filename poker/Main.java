package poker;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poker.controller.PokerTableController;
import poker.model.Poker;
import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.player.Turn;

public class Main extends Application {

   private Stage primaryStage;
   private AnchorPane rootLayout;
   private PokerTableController tableController;
   public Poker poker;

   @Override
   public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Poker");
      showMainView();
      poker = new Poker();
      poker.setMainApp(this);

      Task<Void> task = new Task<Void>() {
         @Override
         public Void call() {
            poker.playPoker();
            return null;
         }
      };

      new Thread(task).start();
   }

   public void showMainView() {

      try {
         // Load root layout from fxml file.
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("view/PokerTable.fxml"));
         rootLayout = (AnchorPane) loader.load();
         rootLayout.setId("pane");

         // Show the scene containing the root layout.
         Scene scene = new Scene(rootLayout);
         scene.getStylesheets().addAll(this.getClass().getResource("view/style.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.show();

         // set controller and application information
         tableController = loader.getController();
         tableController.setMainApp(this);
         tableController.addListener();

      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void updateConsole(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.addText(text);
         }
      });
   }

   public void updatePot(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updatePot(text);
         }
      });
   }

   public void updateStackZero(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateStackZero(text);
         }
      });
   }

   public void updateStackOne(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateStackOne(text);
         }
      });
   }

   public void updateBetAmountZero(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateBetAmountZero(text);
         }
      });
   }

   public void updateBetAmountOne(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateBetAmountOne(text);
         }
      });
   }

   public void toggleDealerZero(boolean dealer) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.toggleDealerZero(dealer);
         }
      });
   }

   public void toggleDealerOne(boolean dealer) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.toggleDealerOne(dealer);
         }
      });
   }

   public void getHoleCards(HoleCards cards) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateHoleCards(cards);
         }
      });
   }

   public void getCommunityCards(ArrayList<Card> cards) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateCommunityCards(cards);
         }
      });
   }

   public String getSuit(Card card) {
      String suit = "view/images/";

      switch (card.getSuit()) {
      case CLUBS:
         suit += "Clubs.png";
         break;
      case DIAMONDS:
         suit += "Diamonds.png";
         break;
      case HEARTS:
         suit += "Hearts.png";
         break;
      case SPADES:
         suit += "Spades.png";
         break;
      }

      return suit;
   }

   public Image getImage(Card card) {
      return new Image(Main.class.getResourceAsStream(getSuit(card)));
   }

   public Turn getPlayerInput() {
      return tableController.getPlayerInput();
   }

   public void disablePlayerInput() {
      tableController.disablePlayerInput();
   }

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public static void main(String[] args) {
      launch(args);
   }
}
