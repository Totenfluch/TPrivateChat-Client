package me.Totenfluch.pack;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import me.Christian.networking.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

public class Main extends Application{
	// Server Stuff
	public static Client connection = null;
	public static InetAddress lComputerIP;
	public static Object ComputerMac;
	public static String ComputerName;
	public static String ComputerIP;
	public static String ConnectToIp = "192.168.178.38";
	public static int ConnectToPort = 9977;
	public static String ActiveUsername;

	// GUI Stuff
	public static TextField text;
	public static TextArea Messages, console;
	public static ListView<String> onlineusers;
	public static TextField Keyfield, Username;
	public static Button Keylock;
	public static Text KeyAmount;
	public String ActiveFont = "Futura";
	public int ActiveFontSize = 20;
	public static final ObservableList<String> names = 
			FXCollections.observableArrayList();

	public static void main(String[] args){
		try {
			lComputerIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		//ComputerMac = getMacAdress();
		ComputerName = lComputerIP.getHostName();
		ComputerIP = lComputerIP.getHostAddress();
		ActiveUsername = ComputerName;
		
		launch(args);
	}

	public void start(Stage primaryStage) {
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;

		BorderPane border = new BorderPane();

		HBox bottomfield = new HBox();

		// BOTTOM
		text = new TextField("");
		text.setPrefSize(900, 20);
		text.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String Text = text.getText();
				if(Text.startsWith(".")){
					String Args[] = Text.split(" ");
					ConsoleCommandParser.parse(Args);
				}else{ 
					if(!(Text.equals(" ") || Text.equals(""))){
						if(Client.IsConnectedToServer){
							String ta = "text ["+ Main.ActiveUsername + "]-> ";
							Text = ta + Text;
							AddToMessageField("<PENDING> " + Text);
							Crypter.doYourThing(Text);
							text.setText("");
						}else{
							AddToMessageField("[Local] " + Text);
							text.setText("");
						}
					}
				}
			}
		});

		bottomfield.setPadding(new Insets(15, 12, 15, 12));
		bottomfield.setSpacing(10);

		bottomfield.getChildren().add(text);
		border.setBottom(bottomfield);

		// Center messages

		HBox centerfield = new HBox();
		Messages = new TextArea();
		Messages.setPrefWidth(700);
		Messages.setEditable(false);
		Messages.setFont(new Font("Futura", 20));
		Messages.setWrapText(true);
		Messages.setStyle("-fx-background-color: Purple;");
		console = new TextArea();
		console.setPrefWidth(300);
		console.setStyle("-fx-background-color: black;");
		console.setEditable(false);
		centerfield.setPadding(new Insets(15, 12, 15, 12));
		centerfield.setSpacing(10);
		centerfield.getChildren().addAll(Messages, console);
		border.setCenter(centerfield);

		// Right side onlinelist

		HBox RightList = new HBox();
		onlineusers = new ListView<>(names);
		
		RightList.setPadding(new Insets(15, 12, 15, 12));
		onlineusers.setItems(names);
		onlineusers.setPrefWidth(150);

		RightList.getChildren().add(onlineusers);

		border.setRight(RightList);

		// TOP

		VBox TopOrgan = new VBox();
		
		HBox TopKey = new HBox();
		Keyfield = new TextField("");
		Keyfield.setPromptText("Enter your Private Key here");
		Keyfield.setPrefWidth(725);
		Keylock = new Button("Lock");
		Keylock.setPrefSize(75, 20);
		Keylock.setDisable(true);
		Keylock.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(Keylock.getText().equals("Lock")){
					Keylock.setText("Unlock");
					Keyfield.setDisable(true);
					AddToConsoleField("[+] Using new Key.");
					Crypter.thekey = Crypter.convertToByteString(Crypter.hashit(Keyfield.getText()));
				}else{
					Keylock.setText("Lock");
					Keyfield.setDisable(false);
				}
			}
		});
		KeyAmount = new Text("0/128");
		Keyfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
					int keyamount = Keyfield.getText().length();
					if(keyamount > 128){
						Keyfield.setText(Keyfield.getText().substring(0, 128));
						keyamount = 128;
					}
					KeyAmount.setText(keyamount+"/128");
					if(keyamount == 128){
						Keylock.setDisable(false);
					}else{
						Keylock.setDisable(true);
					}
			}
		});
		TopKey.setSpacing(10);
		TopKey.getChildren().addAll(Keylock, Keyfield, KeyAmount);
		TopKey.setPadding(new Insets(15, 12, 0, 12));
		
		HBox TopBoxes = new HBox();
		Button UsernameRefresh = new Button("Use");
		UsernameRefresh.setPrefSize(75, 20);
		UsernameRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(Username.getText().length() > 2 && !(Username.getText().startsWith(" ") || Username.getText().startsWith("."))){
					if(Client.IsConnectedToServer){
						Client.processMessage(".namechange " + ActiveUsername + " " + Username.getText());
					}
					ActiveUsername = Username.getText();
					AddToConsoleField("[+] Changed Username");
				}else{
					AddToConsoleField("[-] Failed to changer Username");
				}
			}
		});
		Username = new TextField("");
		Username.setPromptText("Username");
		Username.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
					int keyamount = Username.getText().length();
					if(keyamount > 16){
						Username.setText(Username.getText().substring(0, 16));
					}
			}
		});
		
		Slider FontSize = new Slider();
		FontSize.setMin(0);
		FontSize.setMax(40);
		FontSize.setValue(20);
		FontSize.setShowTickLabels(true);
		FontSize.setShowTickMarks(true);
		FontSize.setMajorTickUnit(10);
		FontSize.setMinorTickCount(1);
		FontSize.setBlockIncrement(5);
		FontSize.setTooltip(new Tooltip("Font Size"));
		FontSize.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number old_val, Number new_val) {
					ActiveFontSize = Integer.valueOf((int) Math.floor(Double.valueOf(new_val.toString())));
					if(ActiveFontSize == 0){
						ActiveFontSize = 1;
					}
					Messages.setFont(new Font(ActiveFont, ActiveFontSize));
			}
		});
		
		final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
		
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Messages.setStyle("-fx-background-color: Purple; -fx-text-fill: "+ toRgbString(colorPicker.getValue()) + ";");
			}
        });
		
		ChoiceBox<String> FontChooser = new ChoiceBox<String>();
		FontChooser.setItems(FXCollections.observableArrayList("Futura", "Arial", "Calibri", "Serif", "Courier New"));
		FontChooser.setTooltip(new Tooltip("Select your favourite font"));
		FontChooser.getSelectionModel().select(0);
		FontChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number old_val, Number new_val) {
					ActiveFont = FontChooser.getSelectionModel().getSelectedItem();
					Messages.setFont(new Font(ActiveFont, ActiveFontSize));
			}
		});
		
		TopBoxes.getChildren().addAll(UsernameRefresh, Username, FontSize, colorPicker, FontChooser);
		
		TopBoxes.setSpacing(10);
		TopBoxes.setPadding(new Insets(15, 12, 0, 12));
		
		TopOrgan.getChildren().addAll(TopKey, TopBoxes);
		
		border.setTop(TopOrgan);
		
		// Global

		Scene s = new Scene(border, 900, 500);

		primaryStage.setScene(s);
		primaryStage.setTitle("Private Messanger, Todays Topic: Do or Die");
		primaryStage.show();
	}

	public static void AddToMessageField(String s){
		Messages.appendText(s+"\n");
	}
	
	private String toRgbString(Color c) {
        return "rgb("
                          + to255Int(c.getRed())
                    + "," + to255Int(c.getGreen())
                    + "," + to255Int(c.getBlue())
             + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

	public static boolean RemoveFromMessageField(String s){
		int n = Messages.getText().length();
		String l = Messages.getText();
		s = "<PENDING> "+s;
		l = l.replace(s, s.replace("<PENDING> text ", ""));
		try{
			Messages.setText(l);
		}catch(Exception e){
			e.printStackTrace();
		}

		Timeline aftertick = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Messages.setScrollTop(Double.MAX_VALUE);
			}
		}));
		aftertick.play();

		int na = l.length();
		if(na == n){
			return false;
		}else{
			return true;
		}

	}

	public static void AddToConsoleField(String s){
		console.appendText(s+"\n");
	}


	public static void DisconnectFromServer(){
		AddToConsoleField("[-] Disconnected from Server");
		try {
			connection.din.close();
			connection.dout.close();
			connection.socket.close();
		}catch (Exception e) {}
		try{connection.socket = null;}catch(Exception e){}
		try{connection.din = null;}catch(Exception e){}
		try{connection.dout = null;}catch(Exception e){}
		try{connection.thread = null;}catch(Exception e){}
		try{connection.running = false;}catch(Exception e){}
		try{connection = null;}catch(Exception e){}
	}
	
	public static boolean ConnectToServer(String ip, int port){
		try{
			connection = new Client(ip, port);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		try{
			if(Client.IsConnectedToServer){
				Client.processMessage(".connect " + Main.ActiveUsername);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}

	public static String getMacAdress(){
		String ComputerMac = null;
		try{
			InetAddress ComputerIP = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ComputerIP);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			ComputerMac = sb.toString();

		}catch ( Exception e){
			e.printStackTrace();
		}
		return ComputerMac;
	}
	
	static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                if(item.startsWith(".")){
                	this.setTextFill(Color.RED);
                	setText(item);
                }
            }
        }
    }
	
	public static void ChangeCells(int whattodo, String item){
		onlineusers.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> arg0) {
				return new ColorRectCell();
			}
		});
	}

}
