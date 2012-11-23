package com.rnb2.test;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
 
 
public class LineDrawing extends Application {
 
  @Override
  public void start(Stage primaryStage) throws Exception {
    final Pane root = new Pane();
    
    root.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        final double x = event.getX();
        final double y = event.getY();
        final BoundLine line = new BoundLine(x, y);
        line.setDrawing(true);
        root.getChildren().add(line);        
      }
    });
    
    primaryStage.setScene(new Scene(root, 600, 600));
    primaryStage.show();
  }
 
  public static void main(String[] args) {launch(args);}
 
  public static class BoundLine extends Line {
    
    private final BooleanProperty drawing = new SimpleBooleanProperty();
    private final EventHandler<MouseEvent> parentDragListener = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (drawing.get()) {
          double x = event.getX();
          double y = event.getY();
          setEndX(x);
          setEndY(y);
        }
      }      
    };
    private final EventHandler<MouseEvent> parentReleaseListener = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        drawing.set(false);
      }
    };
    
    public BoundLine(double initialX, double initialY) {
      super(initialX, initialY, initialX, initialY);
      parentProperty().addListener(new ChangeListener<Parent>() {
        @Override
        public void changed(ObservableValue<? extends Parent> observable,
            Parent oldValue, Parent newValue) {
          if (oldValue != null) {
            oldValue.removeEventHandler(MouseEvent.MOUSE_DRAGGED, parentDragListener);
            oldValue.removeEventHandler(MouseEvent.MOUSE_RELEASED, parentReleaseListener);
          }
          if (newValue != null) {
            newValue.addEventHandler(MouseEvent.MOUSE_DRAGGED, parentDragListener);
            newValue.addEventHandler(MouseEvent.MOUSE_RELEASED, parentReleaseListener);
          }
        }        
      });
      
    }
    
    public BooleanProperty drawingProperty() {
      return drawing ;
    }
    public boolean isDrawing() {
      return drawing.get();
    }
    public void setDrawing(boolean drawing) {
      this.drawing.set(drawing);
    }
  }
}
