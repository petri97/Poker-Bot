package poker;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poker.controller.PokerTableController;
import poker.model.Poker;

public class Main extends Application {

   private Stage primaryStage;
   private AnchorPane rootLayout;
   private PokerTableController tableController;

   @Override
   public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Poker");
      showMainView();
      Poker poker = new Poker();
      poker.setMainApp(this);

      Task task = new Task<Void>() {
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

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public static void main(String[] args) {
      launch(args);
   }
}
