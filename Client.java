import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application{

	
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5,5,5,5));
		pane.setStyle("-fx-border-color: blue");
		pane.setLeft(new Label("Enter your studentID"));
		
		TextField tf = new TextField();
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		pane.setCenter(tf);
		
		BorderPane pane1 = new BorderPane();
		TextArea ta = new TextArea();
		pane1.setCenter(new ScrollPane(ta));
		pane1.setTop(pane);
		
		Scene scene = new Scene(pane1, 600, 400);
		primaryStage.setTitle("Clinet");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		tf.setOnAction( e  -> {
			try{
				String studentID = tf.getText().trim();
				
				toServer.writeUTF(studentID);
				toServer.flush();
				
				String summary = fromServer.readUTF();
				
				ta.appendText("Input is: "+studentID+'\n');
				ta.appendText("Result from server is: "+summary+'\n');
			}
			catch(IOException ex){
				System.err.println(ex); 
			}
		});
		
		try{
			
			Socket socket = new Socket("localhost", 7000);
			
			fromServer = new DataInputStream(socket.getInputStream()); 
			
			toServer = new DataOutputStream(socket.getOutputStream()); 
			
		}
		catch(IOException ex){
			ta.appendText(ex.toString() + '\n'); 
		}
	}
	public static void main(String[] args){
		Application.launch(args);
	}
}
