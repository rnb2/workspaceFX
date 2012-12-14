package com.rnb2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javafx.animation.FillTransition;
import javafx.animation.FillTransitionBuilder;
import javafx.application.Application;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.rnb2.test.GroupText;
import com.rnb2.util.AppUtil;

public class MouseEventsSample extends Application {
	
	protected Properties properties = new Properties();
    private String title = "testF";
	private final String fileName = AppUtil.computeFileName(title, "location ");
	private FillTransition transition;
    //private  AnchorPane pane = null;
    private static final String resourcePath = "/com/rnb2/resources/fxml/";
    private Button buttonAdd = new Button("Truba");
    private Button buttonLabel = new Button("Label");
    private TextField textField1 = new TextField();
	private GroupText groupText;
	
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
    	final Group root = new Group();
      	       
    	AnchorPane pane = null;
    	AnchorPane pane2 = null;
    	
    	 int i = getElementIndex(root);
    	
		try {
			pane = createPane(resourcePath +  "truba2.fxml", String.valueOf(i));
			//pane2 = createPane(resourcePath +  "truba2.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final ArrayList<AnchorPane> panes = new ArrayList<AnchorPane>();
		//panes.add(pane2);
		panes.add(pane);
		
    	
    	System.out.println("pane " + pane);
    	
    	Rectangle trubaPart =  lookup(pane, "#truba2_1", Rectangle.class);
    	System.out.println("trubaPart.getHeight(): " + trubaPart.getWidth());
    	//trubaPart.setWidth(10.0);
    	
    	Group truba2 = lookup(pane, "#Group", Group.class);
    	truba2.setAutoSizeChildren(true);
    	truba2.setScaleY(1.5);
    	truba2.setScaleX(1.5);
    	
    	
    	
    	transition = FillTransitionBuilder.create()

                 .duration(Duration.seconds(1))

                 .fromValue(Color.DODGERBLUE)

                 .toValue(Color.AQUA)

                 //.cycleCount(Timeline.INDEFINITE)

                 //.autoReverse(true)

                 .build();

        

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
        final Circle circleSmall = createCircle("Blue_circle", Color.DODGERBLUE, 25);
        initShapeLocation(circleSmall);
        
       // textField1.textProperty().bindBidirectional((Property<String>) circleSmall.radiusProperty().asString());
    
        

        final Circle circleBig = createCircle("Orange_circle", Color.CORAL, 40);
        initShapeLocation(circleBig);

        // we can set mouse event to any node, also on the rectangle

        rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                //log mouse move to console, method listed below

                showOnConsole("Mouse moved, x: " + me.getX() + ", y: " + me.getY() );

            }

        });

 
        root.setOnMouseClicked(rootMouseEvent);
        
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
        
        final HBox hBox1 = new HBox(5.0);
        Label label1 = new Label("Radius:");
        hBox1.getChildren().addAll(label1, textField1);
        
        
        final VBox vBoxLeft = new VBox();
        vBoxLeft.setSpacing(10);
        
        vBoxLeft.getChildren().addAll(buttonAdd, hBox1);
        
        buttonAdd.setPrefSize(90, 20);
        buttonAdd.setStyle("-fx-base: red;");

        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		
            	 try {
            		 int i= getElementIndex(root);
            		 String id = "truba_" + String.valueOf(i);
					AnchorPane createPane = createPane(resourcePath +  "truba2.fxml", id);
            		 initShapeLocation(createPane);

            		 root.getChildren().add(createPane);

             		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
            	 
            }

        });
        
        buttonLabel.setPrefSize(90, 20);
        buttonLabel.setStyle("-fx-base: red;");

        buttonLabel.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent t) {

            	 try {
            		 int i= getElementIndex(root);
            		groupText = new GroupText("Text_".concat(String.valueOf(i)), 10, 10, 100, 30);
            		 	groupText.setTextColor(Color.DARKGREEN);
	                 	groupText.setStroke(Color.DARKGRAY);
	                 	groupText.setFill(Color.WHITESMOKE);
	                 	groupText.setTextFontSize(14);
	                 	
	                 	groupText.bind(textField1);
	                 	
            		 root.getChildren().add(groupText);
            		 
            		// textField1.textProperty().bind(groupText.textProperty());

             		} catch (Exception e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
            	 
            }

        });
        
        vBoxLeft.getChildren().add(buttonLabel);
        
        
       
        // show all the circle , rectangle and console

        root.getChildren().addAll(rectangle, circleBig, circleSmall, console);
        
       // for(AnchorPane p: panes){
       // 	root.getChildren().add(p);
      //  }
        
        root.getChildren().add(vBoxLeft);

       
    }
	
	private<T> int getElementIndex(Group root){
		int i=0;
		 for(Node node: root.getChildren()){
			 if (node instanceof AnchorPane) {
				AnchorPane new_name = (AnchorPane) node;
				i++;
			}else if(node instanceof Group){
				
				i++;
			}
		 }	 
		return i;	 
	}	
    
	private void initShapeLocation(AnchorPane shape) {
		// TODO Auto-generated method stub
    	String keyX = shape.getId() + "-x";
		String propertyX = properties.getProperty(keyX, new Integer(200).toString());
        String keyY = shape.getId() + "-y";
		String propertyY = properties.getProperty(keyY, new Integer(80).toString());
		
        shape.setTranslateX(Double.parseDouble(propertyX));
        shape.setTranslateY(Double.parseDouble(propertyY));
		
	}
	
    private void initShapeLocation(Shape shape) {
		// TODO Auto-generated method stub
    	String keyX = shape.getId() + "-x";
		String propertyX = properties.getProperty(keyX, new Integer(200).toString());
        String keyY = shape.getId() + "-y";
		String propertyY = properties.getProperty(keyY, new Integer(80).toString());
		
        shape.setTranslateX(Double.parseDouble(propertyX));
        shape.setTranslateY(Double.parseDouble(propertyY));
		
	}
    
    private void savePosition(AnchorPane pane) {
		FileOutputStream out = null;
		try{
			out = new FileOutputStream(fileName);
			String keyX = pane.getId() + "-x";
			properties.put(keyX, new Double(pane.getTranslateX()).toString());
			String keyY = pane.getId() + "-y";
			properties.put(keyY, new Double(pane.getTranslateY()).toString());
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
    
    private void savePosition(Shape circle) {
		FileOutputStream out = null;
		try{
			out = new FileOutputStream(fileName);
			String keyX = circle.getId() + "-x";
			properties.put(keyX, new Double(circle.getTranslateX()).toString());
			String keyY = circle.getId() + "-y";
			properties.put(keyY, new Double(circle.getTranslateY()).toString());
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

	@SuppressWarnings("unchecked")
	public <T> T lookup(Node parent, String id, Class<T> clazz) {
        for (Node node : parent.lookupAll(id)) {
            if (node.getClass().isAssignableFrom(clazz)) {
                return (T)node;
            }
        }
        throw new IllegalArgumentException("Parent " + parent + " doesn't contain node with id " + id);
    }
    

    private double newYPosition;

    private AnchorPane createPane(String formName, String id) throws IOException{
	
  
		URL url = getClass().getResource(formName);
		final AnchorPane pane = (AnchorPane) FXMLLoader.load(url);
	    
		pane.setId(id);
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {

             public void handle(MouseEvent me) {

                  //when mouse is pressed, store initial position

                 initX = pane.getTranslateX();

                 initY = pane.getTranslateY();

                 dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());

                // showOnConsole("Mouse pressed above " + name);

             }

         });
         
         pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

        	 public void handle(MouseEvent event) {
         		
         		 double dragX = event.getSceneX() - dragAnchor.getX();
                 double dragY = event.getSceneY() - dragAnchor.getY();

                  newXPosition = initX + dragX;
                  newYPosition = initY + dragY;

                  //if new position do not exceeds borders of the rectangle, translate to this position
                  pane.setTranslateX(newXPosition);
                  pane.setTranslateY(newYPosition);

         	}
 		});
    	
         pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
 			public void handle(MouseEvent me) {
                 final DropShadow dropShadow = new DropShadow();
                 pane.setEffect(dropShadow);
             }

         });

         pane.setOnMouseExited(new EventHandler<MouseEvent>() {
             public void handle(MouseEvent me) {
                 pane.setEffect(null);
             }

         });
         
         pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
             public void handle(MouseEvent me) {
                 savePosition(pane);
             }

         });

    	return pane;
    }
    
    private Circle createCircle(final String name, final Color color, int radius) {

        //create a circle with desired name,  color and radius

        RadialGradient radialGradient = new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {

            new Stop(0, Color.rgb(250,250,255)),

            new Stop(1, color)

        });
        
		final Circle circle = new Circle(radius, radialGradient);

		circle.setId(name);
        //add a shadow effect
		 showOnConsole("circle id is:" + circle.getId());

        //circle.setEffect(new InnerShadow(7, color.darker().darker()));
		 circle.setEffect(null);

        //change a cursor when it is over circle

        circle.setCursor(Cursor.HAND);

        //add a mouse listeners

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                showOnConsole("Clicked on" + name + ", " + me.getClickCount() + "times radius:" + circle.getRadius());
               
                textField1.textProperty().bind(circle.idProperty());
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
                    //showOnConsole(name + " was dragged (x:" + newXPosition );
                }

                if ((newYPosition>=circle.getRadius()) && (newYPosition<=300-circle.getRadius())){

                    circle.setTranslateY(newYPosition);
                    //showOnConsole(name + " was dragged, y:" + newYPosition +")");
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

                savePosition(circle);
            }

        });

 

        return circle;

    }

 

    EventHandler<MouseEvent> rootMouseEvent = new EventHandler<MouseEvent>() {
    	public void handle(MouseEvent event) {
    		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
    			Object obj = selectObject(event.getSource());
    			System.out.println("obj = " + obj);
    		}
    		
    		
    	}
    };
	
    protected Object selectObject(Object source) {
    	if(source == null){
    		return null;
    	}
    	if(source instanceof Circle){
    		return source;
    	}
    	if(source instanceof Group){
    		Group obj  = (Group) source;
    		
    		DropShadow dropShadow = new DropShadow();
    		obj.setEffect(dropShadow);
    	}
    	return null;
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

    public static void main(String[] args) {
    	         
    	launch(args); 
    }
}
