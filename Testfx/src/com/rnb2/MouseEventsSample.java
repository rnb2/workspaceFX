package com.rnb2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.rnb2.util.AppUtil;
import com.sun.scenario.effect.ColorAdjust;

import javafx.animation.FillTransition;
import javafx.animation.FillTransitionBuilder;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MouseEventsSample extends Application {
	
	protected Properties properties = new Properties();
    private String title = "testF";
	private final String fileName = AppUtil.computeFileName(title, "location ");
	private FillTransition transition;
	
	//create a console for logging mouse events

    final ListView<String> console = new ListView<String>();

    //create a observableArrayList of logged events that will be listed in console

    final ObservableList<String> consoleObservableList = FXCollections.observableArrayList();

    {

        //set up the console

        console.setItems(consoleObservableList);

        console.setLayoutY(305);

        console.setPrefSize(450, 195);

    }

    //create a rectangle - (450px X 300px) in which our circles can move

    final Rectangle rectangle = RectangleBuilder.create()

            .width(450).height(300)

            .fill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] {

                new Stop(1, Color.rgb(156,216,255)),

                new Stop(0, Color.rgb(156,216,5, 1.0))

            }))

            .stroke(Color.BLACK)

            .build();

    //variables for storing initial position before drag of circle

    private double initX;

    private double initY;

    private Point2D dragAnchor;

    private double newXPosition;
 

    private void init(Stage primaryStage) {
    	
    	
    	transition = FillTransitionBuilder.create()

                 .duration(Duration.seconds(1))

                 .fromValue(Color.DODGERBLUE)

                 .toValue(Color.AQUA)

                 //.cycleCount(Timeline.INDEFINITE)

                 //.autoReverse(true)

                 .build();

        Group root = new Group();

        primaryStage.setResizable(false);

        primaryStage.setScene(new Scene(root, 460,500));
        
        //----------
        
		File file = new File(fileName);
		
		File path = file.getParentFile();
		if (!path.exists()){
			path.mkdirs();
		}
		try{
			FileInputStream propf = new FileInputStream(file);
			properties.load(propf);
			propf.close();
		}
		 catch(FileNotFoundException exception){}
		 catch(IOException exception){ }
													

        // create circle with method listed below: paramethers: name of the circle, color of the circle, radius

        final Circle circleSmall = createCircle("Blue circle", Color.DODGERBLUE, 25);

        
		circleSmall.setTranslateX(Double.parseDouble(properties.getProperty("x", new Integer(200).toString())));//200);

        circleSmall.setTranslateY(Double.parseDouble(properties.getProperty("y", new Integer(80).toString())));//80);

        // and a second, bigger circle

        final Circle circleBig = createCircle("Orange circle", Color.CORAL, 40);

        circleBig.setTranslateX(300);

        circleBig.setTranslateY(150);

        // we can set mouse event to any node, also on the rectangle

        rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                //log mouse move to console, method listed below

                //showOnConsole("Mouse moved, x: " + me.getX() + ", y: " + me.getY() );

            }

        });

 

        root.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override public void handle(ScrollEvent event) {

                double translateX = event.getDeltaX();

                double translateY = event.getDeltaY();

 

                // reduce the deltas for the circles to stay in the screen

                for (Circle c : new Circle[] { circleSmall, circleBig }) {

                    if (c.getTranslateX() + translateX + c.getRadius() > 450) {

                        translateX = 450 - c.getTranslateX() - c.getRadius();

                    }

                    if (c.getTranslateX() + translateX - c.getRadius() < 0) {

                        translateX = - c.getTranslateX() + c.getRadius();

                    }

                    if (c.getTranslateY() + translateY + c.getRadius() > 300) {

                        translateY = 300 - c.getTranslateY() - c.getRadius();

                    }

                    if (c.getTranslateY() + translateY - c.getRadius() < 0) {

                        translateY = - c.getTranslateY() + c.getRadius();

                    }

                }

 

                // move the circles

                for (Circle c : new Circle[] { circleSmall, circleBig }) {

                    c.setTranslateX(c.getTranslateX() + translateX);

                    c.setTranslateY(c.getTranslateY() + translateY);

                }

 

                // log event

                showOnConsole("Scrolled, deltaX: " + event.getDeltaX() +

                        ", deltaY: " + event.getDeltaY());

            }

        });

        // show all the circle , rectangle and console

        root.getChildren().addAll(rectangle, circleBig, circleSmall, console);

    }

    private double newYPosition;

    private Circle createCircle(final String name, final Color color, int radius) {

        //create a circle with desired name,  color and radius

        RadialGradient radialGradient = new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {

            new Stop(0, Color.rgb(250,250,255)),

            new Stop(1, color)

        });
        
		final Circle circle = new Circle(radius, radialGradient);

        //add a shadow effect

        circle.setEffect(new InnerShadow(7, color.darker().darker()));

        //change a cursor when it is over circle

        circle.setCursor(Cursor.HAND);

        //add a mouse listeners

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                showOnConsole("Clicked on" + name + ", " + me.getClickCount() + "times");

                //the event will be passed only to the circle which is on front

                me.consume();

            }

        });

        
        
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {

          
			

			public void handle(MouseEvent me) {

                double dragX = me.getSceneX() - dragAnchor.getX();

                double dragY = me.getSceneY() - dragAnchor.getY();

                //calculate new position of the circle

                newXPosition = initX + dragX;

                newYPosition = initY + dragY;

                //if new position do not exceeds borders of the rectangle, translate to this position

                if ((newXPosition>=circle.getRadius()) && (newXPosition<=450-circle.getRadius())) {

                    circle.setTranslateX(newXPosition);
                    showOnConsole(name + " was dragged (x:" + newXPosition );
                }

                if ((newYPosition>=circle.getRadius()) && (newYPosition<=300-circle.getRadius())){

                    circle.setTranslateY(newYPosition);
                    showOnConsole(name + " was dragged, y:" + newYPosition +")");
                }

               

            }

        });

        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {

            

			public void handle(MouseEvent me) {

                //change the z-coordinate of the circle

                circle.toFront();
                
               
                transition.setShape(circle);
                transition.play();
                
                showOnConsole("Mouse entered " + name);

            }

        });

        circle.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                showOnConsole("Mouse exited " +name);
                transition.stop();
                RadialGradient radialGradient = new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {

                        new Stop(0, Color.rgb(250,250,255)),

                        new Stop(1, color)

                    });
                    
                circle.setFill(radialGradient);
            }

        });

        circle.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                 //when mouse is pressed, store initial position

                initX = circle.getTranslateX();

                initY = circle.getTranslateY();

                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());

                showOnConsole("Mouse pressed above " + name);

            }

        });

        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                showOnConsole("Mouse released above " +name + " x: " + circle.getTranslateX() + " y: " + circle.getTranslateY());

                FileOutputStream out = null;
				try{
					out = new FileOutputStream(fileName);
					properties.put("x", new Double(circle.getTranslateX()).toString());
					properties.put("y", new Double(circle.getTranslateY()).toString());
					properties.store(out, title);
				}
				
				catch(FileNotFoundException exception){}
				catch(IOException exception){}
				
				finally{
					if (out != null){
						try { 
							out.flush(); 
							out.close(); 
						} catch(Exception exception){}
					} 
										
				}
				
            }

        });

 

        return circle;

    }

 
    
    private void showOnConsole(String text) {

         //if there is 8 items in list, delete first log message, shift other logs and  add a new one to end position

         if (consoleObservableList.size()==8) {

            consoleObservableList.remove(0);

         }

         consoleObservableList.add(text);

    }

 

    public double getSampleWidth() { return 500; }

 

    public double getSampleHeight() { return 500; }

 

    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }
}
