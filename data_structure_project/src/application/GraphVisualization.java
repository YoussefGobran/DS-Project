package application;

import application.helpers.XMLNode;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import java.util.List;

public class GraphVisualization {
    private XMLNode rootNode;

    public GraphVisualization(XMLNode rootNode) {
        this.rootNode = rootNode;
    }

    public void showGraph() {
        Pane pane = new Pane();

        List<XMLNode> users = rootNode.getChildrenNodes();
        int numUsers = users.size();
        double centerX = 300;
        double centerY = 200;
        double radius = 150;

        for (int i = 0; i < numUsers; i++) {
            double angle = 2 * Math.PI * i / numUsers;
            double userX = centerX + radius * Math.cos(angle);
            double userY = centerY + radius * Math.sin(angle);

            XMLNode user = users.get(i);
            String userId = user.getChildrenNodes().get(0).getInnerText(); // Assuming the first child is the user ID

            Circle userCircle = new Circle(20, Color.POWDERBLUE);
            userCircle.setCenterX(userX);
            userCircle.setCenterY(userY);

            Label userIdLabel = new Label(userId);
            userIdLabel.setLayoutX(userX - 12); // Adjust label position
            userIdLabel.setLayoutY(userY - 12); // Adjust label position

            pane.getChildren().addAll(userCircle, userIdLabel);
        }

        // Draw lines (arrows) from users to followers
        for (XMLNode user : users) {
            String userId = user.getChildrenNodes().get(0).getInnerText();
            List<XMLNode> followers = user.getChildrenNodes().get(3).getChildrenNodes(); // Assuming the fourth child is the followers node

            for (XMLNode follower : followers) {
                String followerId = follower.getChildrenNodes().get(0).getInnerText(); // Assuming the first child is the follower ID

                Circle userCircle = findUserCircle(pane, users, userId);
                Circle followerCircle = findUserCircle(pane, users, followerId);

                if (userCircle != null && followerCircle != null) {
                    // Draw line only if the follower ID matches an existing user ID
                    if (findUserCircle(pane, users, followerId) != null) {
                        ArrowLine arrowLine = new ArrowLine(pane, followerCircle.getCenterX(), followerCircle.getCenterY(),
                                userCircle.getCenterX(), userCircle.getCenterY());
                        pane.getChildren().add(arrowLine);
                    }
                }
            }
        }

        Scene scene = new Scene(pane, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Graph Visualization");
        stage.show();
    }

    private Circle findUserCircle(Pane pane, List<XMLNode> users, String userId) {
        for (XMLNode user : users) {
            String currentUserId = user.getChildrenNodes().get(0).getInnerText();
            if (userId.equals(currentUserId)) {
                double userX = ((Circle) pane.getChildren().get(users.indexOf(user) * 2)).getCenterX();
                double userY = ((Circle) pane.getChildren().get(users.indexOf(user) * 2)).getCenterY();
                return new Circle(userX, userY, 20);
            }
        }
        return null;
    }

    private static class ArrowLine extends Line {

        private static final double ARROW_SIZE = 10;
        private final Pane pane;

        public ArrowLine(Pane pane, double startX, double startY, double endX, double endY) {
            super(startX, startY, endX, endY);
            this.pane = pane;
            drawArrowHead();
        }

        private void drawArrowHead() {
            double angle = Math.atan2(getEndY() - getStartY(), getEndX() - getStartX());

            double arrowEndX = getStartX() + ARROW_SIZE * Math.cos(angle);
            double arrowEndY = getStartY() + ARROW_SIZE * Math.sin(angle);

            Polygon arrowhead = new Polygon(
                    arrowEndX, arrowEndY,
                    arrowEndX + ARROW_SIZE * Math.cos(angle - Math.toRadians(45)),
                    arrowEndY + ARROW_SIZE * Math.sin(angle - Math.toRadians(45)),
                    arrowEndX + ARROW_SIZE * Math.cos(angle + Math.toRadians(45)),
                    arrowEndY + ARROW_SIZE * Math.sin(angle + Math.toRadians(45))
            );

            pane.getChildren().add(arrowhead);
        }
    }



}
