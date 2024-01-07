package application;

import application.helpers.XMLNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Line;
import java.util.List;
import application.helpers.User;

public class GraphVisualization {
    private List<User> users;
    private List<List<Integer>> adjacencyListUsers;

    public GraphVisualization(List<User> users, List<List<Integer>> adjacencyListUsers) {
        this.users = users;
        this.adjacencyListUsers = adjacencyListUsers;
    }

    public void showGraph() {
        Pane totalPane = new Pane();

        Pane circlesPane = new Pane();
        Pane linesPane = new Pane();
        Pane arrowsPane = new Pane();

        double[][] usersCoordinates = new double[1000][2];
        int numUsers = users.size();
        double centerX = 300;
        double centerY = 200;
        double radius = 150;

        double ARROW_SIZE = 10;

        for (int i = 0; i < numUsers; i++) {
            double angle = 2 * Math.PI * i / numUsers;
            double userX = centerX + radius * Math.cos(angle);
            double userY = centerY + radius * Math.sin(angle);

            int userId = users.get(i).id;

            usersCoordinates[userId][0] = userX;
            usersCoordinates[userId][1] = userY;

            Circle userCircle = new Circle(20, Color.POWDERBLUE);
            userCircle.setCenterX(userX);
            userCircle.setCenterY(userY);

            Label userIdLabel = new Label(String.valueOf(userId));
            userIdLabel.setAlignment(Pos.CENTER);
            userIdLabel.setTextAlignment(TextAlignment.CENTER);
            userIdLabel.setLayoutX(userX-6); // Adjust label position
            userIdLabel.setLayoutY(userY-8); // Adjust label position

            circlesPane.getChildren().addAll(userCircle, userIdLabel);
        }

        // Draw lines (arrows) from users to followers
        for (int i = 0; i < adjacencyListUsers.size(); i++) {
            for (Integer followerID : adjacencyListUsers.get(i)) {
                double startX=usersCoordinates[followerID][0];
                double startY=usersCoordinates[followerID][1];
                double endX= usersCoordinates[i][0];
                double endY=usersCoordinates[i][1];
                linesPane.getChildren().addAll(new Line(startX,startY,endX,endY));

                double angle = Math.atan2(endY - startY, endX - startX);

                double arrowEndX = startX + ARROW_SIZE * Math.cos(angle);
                double arrowEndY = startY + ARROW_SIZE * Math.sin(angle);

                Polygon arrowhead = new Polygon(
                        arrowEndX, arrowEndY,
                        arrowEndX + ARROW_SIZE * Math.cos(angle - Math.toRadians(45)),
                        arrowEndY + ARROW_SIZE * Math.sin(angle - Math.toRadians(45)),
                        arrowEndX + ARROW_SIZE * Math.cos(angle + Math.toRadians(45)),
                        arrowEndY + ARROW_SIZE * Math.sin(angle + Math.toRadians(45)));

                arrowsPane.getChildren().addAll(arrowhead);

            }

        }
        totalPane.getChildren().addAll(linesPane, circlesPane, arrowsPane);
        Scene scene = new Scene(totalPane, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Graph Visualization");
        stage.show();
    }

}
