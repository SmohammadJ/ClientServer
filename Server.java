import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
	private static final File file = new File("D:\\workspace\\Final_file.txt");

	
	TextArea ta = new TextArea();
	
	private int clientNo = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		Scene scene = new Scene(new ScrollPane(ta), 500, 200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread( () -> {
			try {
				ServerSocket serversocket = new ServerSocket(7000);
				ta.appendText("Server Started \n");
				
				while(true){
					Socket socket = serversocket.accept();
					clientNo++;
					
					Platform.runLater( () -> {
						ta.appendText("Stariting thread for client: "+ clientNo+'\n');
						
						InetAddress inetAddress = socket.getInetAddress();
						ta.appendText("Client "+ clientNo+ "'s IP Address is: "+inetAddress.getHostAddress()+'\n');
					});
					
					new Thread(new HandleClients(socket)).start();
					
				}
			}
			catch(IOException ex) {
				 System.err.println(ex);
			}
		}).start();
	}
	
	class HandleClients implements Runnable{
		
		
		private Socket socket;
		
	    public HandleClients(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try{
				
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				
				
				while(true){
					
					String studentID = inputFromClient.readUTF();
					
					String summary = searchFile(studentID);
					
					outputToClient.writeUTF(summary);
					
					Platform.runLater(() -> {
						ta.appendText("studentID received from client: "+ studentID+'\n');
						ta.appendText("the result is: "+summary+'\n');
					});	
				}
			}
			catch(IOException  ex){
				ex.printStackTrace(); 
			}
		}

		private String searchFile(String studentID) {
			String txt = "not found";
			try {
				Scanner scan = new Scanner(file);
				while(scan.hasNext()){
					String summaries = scan.nextLine().toString();
					if(summaries.contains(studentID)) txt = summaries;
				}
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			return txt;	
		}
	}
	
	public static void main(String[] args){
		Application.launch(args);
	}
}

	
