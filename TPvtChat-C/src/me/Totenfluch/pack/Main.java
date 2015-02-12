package me.Totenfluch.pack;



import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.Christian.networking.Client;
import javafx.scene.control.CheckBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

public class Main extends Application{
	// Server Stuff
	public static Client connection = null;
	private static InetAddress lComputerIP;
	private static String ComputerName;
	public static String ActiveUsername;

	// GUI Stuff
	public static Stage primstage;
	public static TextField TextInputField;
	public static TextArea console;
	public static ScrollPane messageSP;
	public static HBox[] messagelist = new HBox[50];
	public static ListView<String> onlineusers;
	public static TextField Keyfield, Username, MessageSendDelayField, ChannelField, ChannelPasswordField;
	public static CheckBox DontSend, StyleChooser;
	public static Button Keylock, OpenOptions;
	public static Text KeyAmount;
	public String ActiveFont = "Futura";
	public int ActiveFontSize = 20;
	public static VBox content;
	public static HBox centerfield;
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
		ActiveUsername = Crypter.hashit(ComputerName).substring(0, 16);

		launch(args);
	}

	public void start(Stage primaryStage) {
		primstage = primaryStage;
		Main.primstage.getIcons().add(new Image("blue.png"));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;


		BorderPane border = new BorderPane();

		HBox bottomfield = new HBox();

		// BOTTOM
		TextInputField = new TextField("");
		TextInputField.setPrefHeight(20);
		TextInputField.setPrefWidth(6000);
		TextInputField.setFont(Font.font("Futura", 13));
		TextInputField.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String Text = TextInputField.getText();
				if(Text.startsWith(".")){
					String Args[] = Text.split(" ");
					ConsoleCommandParser.parse(Args);
				}else{ 
					if(!(Text.equals(" ") || Text.equals(""))){
						if(!MessageSendDelayField.getText().equals("")){
							if(Integer.valueOf(MessageSendDelayField.getText()) > 0){
								if(Client.IsConnectedToServer){
									if(StyleChooser.isSelected()){
										final int wn = Integer.valueOf(MessageSendDelayField.getText());
										final TextField xp = AddToMessageField("<"+wn+"> ["+Main.ActiveUsername+"]-> " + Text , 0);
										final String ourtext = "["+Main.ActiveUsername+"]-> " + Text;
										Timeline aftertickz = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent arg0) {
												int pn = Integer.valueOf(xp.getText().split("<")[1].split(">")[0]);
												if(pn > 0){
													pn--;
													xp.setText("<"+pn+">"+ourtext);
												}else{
													xp.setText("<PENDING> text " + ourtext);
													if(Client.IsConnectedToServer){
														Crypter.doYourThing("text "+ ourtext);
													}else{
														xp.setText("<Local> "+ourtext);
													}
												}
											}
										}));
										aftertickz.setCycleCount(wn+1);
										aftertickz.play();
										TextInputField.setText("");
									}else{
										final int wn = Integer.valueOf(MessageSendDelayField.getText());
										final String ta = "text ["+ Main.ActiveUsername + "]-> " + Text;
										AlternativeAddToMessageField("<PENDING> " + ta, 0, Main.ActiveUsername);
										Timeline aftertickz = new Timeline(new KeyFrame(Duration.seconds(wn), new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent arg0) {
												if(!DontSend.isSelected()){
													Crypter.doYourThing(ta);
												}else{
													RemoveFromMessageField(ta);
												}
											}
										}));
										aftertickz.play();
										TextInputField.setText("");
									}
								}else{
									if(StyleChooser.isSelected()){
										AddToMessageField("<Local> [" + ActiveUsername + "]->" + Text, 0);
										TextInputField.setText("");
									}else{
										AlternativeAddToMessageField("<Local> " + Text, 0, ActiveUsername);
										TextInputField.setText("");
									}
								}
							}
						}else if(Client.IsConnectedToServer){
							if(StyleChooser.isSelected()){
								String ta = "text ["+ Main.ActiveUsername + "]-> ";
								Text = ta + Text;
								if(Crypter.doYourThing(Text)){
									AddToMessageField("<PENDING> " + Text, 0);
								}
								TextInputField.setText("");
							}else{
								String ta = "text ["+ Main.ActiveUsername + "]-> ";
								Text = ta + Text;
								if(Crypter.doYourThing(Text)){
									AlternativeAddToMessageField("<PENDING> " + Text, 0, Main.ActiveUsername);
								}
								TextInputField.setText("");
							}
						}else{
							if(StyleChooser.isSelected()){
								AddToMessageField("<Local> [" + ActiveUsername + "]->" + Text, 0);
								TextInputField.setText("");
							}else{
								AlternativeAddToMessageField("<Local> " + Text, 0, ActiveUsername);
								TextInputField.setText("");
							}
						}
					}
				}
			}
		});

		bottomfield.setPadding(new Insets(15, 12, 15, 12));
		bottomfield.setSpacing(10);
		bottomfield.setAlignment(Pos.CENTER);

		bottomfield.getChildren().add(TextInputField);
		border.setBottom(bottomfield);

		// Center messages

		centerfield = new HBox();
		centerfield.setAlignment(Pos.CENTER);


		messageSP = new ScrollPane();
		messageSP.setFitToWidth(false);
		messageSP.setPadding(new Insets(0, 2, 2, 10));
		messageSP.setStyle("-fx-background: transparent");
		messageSP.setStyle("-fx-background-color:transparent;");
		messageSP.setStyle("-fx-background: white;");

		content = new VBox();
		content.setPadding(new Insets(5, 5, 5, 10));
		content.setSpacing(10);
		content.setAlignment(Pos.TOP_LEFT);

		content.setMaxWidth(primaryStage.getWidth()-268);
		content.setPrefWidth(primaryStage.getWidth()-268);

		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> arg0,
					Number old, Number nnew) {
				if(nnew.intValue() < 300){
					primaryStage.setWidth(300);
				}else{
					if(Main.console.getParent() == null){
						content.setMaxWidth(nnew.intValue()-268);
						content.setPrefWidth(nnew.intValue()-268);
					}else{
						content.setMaxWidth(nnew.intValue()-478);
						content.setPrefWidth(nnew.intValue()-478);
					}

					for(int i = 0; i < messagelist.length; i++){
						if(messagelist[i] != null) {
							messagelist[i].setPrefWidth(content.getPrefWidth()-10);
							messagelist[i].setMaxWidth(content.getMaxWidth()-10);
							ObservableList<Node> ovbn = messagelist[i].getChildren();
							if(ovbn.size() == 1){
								for(Node n : ovbn){
									TextField f = (TextField)n;
									f.setPrefWidth(content.getPrefWidth()-10);
									f.setMaxWidth(content.getMaxWidth()-10);
								}
							}
						}
					}
				}
			}
		});

		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number nnew) {
				if(nnew.intValue() < 200){
					primaryStage.setHeight(200);
				}

			}
		});



		messageSP.setContent(content);


		console = new TextArea();
		console.setMaxWidth(200);
		console.setStyle("-fx-background-color: black;");
		console.setEditable(false);
		console.setWrapText(true);

		content.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				messageSP.setVvalue(messageSP.getVmax());
			}
		});

		centerfield.setPadding(new Insets(15, 12, 15, 12));
		centerfield.setSpacing(10);

		VBox ButtonLeft = new VBox();

		OpenOptions = new Button("<");
		OpenOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				String[] xn = {".toggle", "console"};
				ConsoleCommandParser.parse(xn);
			}
		});
		OpenOptions.setPrefHeight(1920);
		ButtonLeft.setPadding(new Insets(15, 0, 12, 12));

		ButtonLeft.getChildren().add(OpenOptions);

		border.setLeft(ButtonLeft);

		centerfield.getChildren().add(messageSP);


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
					Crypter.createKeys();
				}else{
					Keylock.setText("Lock");
					Keyfield.setDisable(false);
				}
			}
		});
		KeyAmount = new Text("0/128");
		KeyAmount.setFont(Font.font("Futura", FontWeight.BOLD, 12));
		KeyAmount.setFill(Color.RED);
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
					KeyAmount.setFill(Color.LIME);
				}else{
					Keylock.setDisable(true);
					KeyAmount.setFill(Color.RED);
				}
			}
		});
		Keyfield.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(!Keylock.isDisabled()){
					if(Keylock.getText().equals("Lock")){
						Keylock.setText("Unlock");
						Keyfield.setDisable(true);
						AddToConsoleField("[+] Using new Key.");
						Crypter.createKeys();
					}else{
						Keylock.setText("Lock");
						Keyfield.setDisable(false);
					}
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
				setUsername();
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
		Username.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setUsername();
			}
		});

		Text MessageSendDelayText = new Text();
		MessageSendDelayText.setText("Message send\ndelay in s");
		MessageSendDelayText.setFont(Font.font("Futura", FontWeight.BOLD, 12));
		MessageSendDelayText.setFill(Color.LIME);

		MessageSendDelayField = new TextField("");
		MessageSendDelayField.setPromptText("(1-10^6) | 5 = 5s");
		MessageSendDelayField.setPrefWidth(115);
		MessageSendDelayField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(!arg2.matches("[Z0-9]+") && !arg2.equals("")){
					MessageSendDelayField.setText(arg1);
				}
				if(arg2.length() > 7){
					MessageSendDelayField.setText(arg1);
				}
			}
		});

		DontSend = new CheckBox("DONT SEND");
		DontSend.setFont(Font.font("Futura", FontWeight.BOLD, 9));

		DontSend.setStyle("-fx-text-fill:red;");

		DontSend.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				if(new_val){
					border.setStyle("-fx-background-color: RED");
					DontSend.setStyle("-fx-text-fill: black");
					Main.AddToConsoleField("[!] Blocking ALL <PENDING> Messages");
				}else{
					border.setStyle("-fx-background-image: url('lol.jpg'); -fx-background-position: center center; -fx-background-size: 900 600;");
					Main.AddToConsoleField("[!] Allowing ALL <PENDING> Messages");
					DontSend.setStyle("-fx-text-fill: red");
				}
			}
		});

		StyleChooser = new CheckBox("Old Style");
		StyleChooser.setFont(Font.font("Futura", FontWeight.BOLD, 9));
		StyleChooser.setStyle("-fx-text-fill: lime;");

		border.setStyle("-fx-background-image: url('lol.jpg'); -fx-background-position: center center; -fx-background-size: 900 600;");


		TopBoxes.getChildren().addAll(UsernameRefresh, Username, /*FontSize, colorPicker, FontChooser,*/ MessageSendDelayText, MessageSendDelayField, DontSend, StyleChooser);

		TopBoxes.setSpacing(10);
		TopBoxes.setPadding(new Insets(15, 12, 0, 12));


		HBox ChannelBox = new HBox();

		Button SwitchChannel = new Button("Switch");
		SwitchChannel.setPrefSize(75, 20);
		SwitchChannel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				AddToConsoleField("[~] Attempting to switch channel to: '"+ChannelField.getText()+"'");
				SwitchChannel(ChannelField.getText(), ChannelPasswordField.getText());
			}
		});


		ChannelField = new TextField();
		ChannelField.setPrefWidth(359);
		ChannelField.setPromptText("Channel Name");

		ChannelPasswordField = new TextField();
		ChannelPasswordField.setPrefWidth(355);
		ChannelPasswordField.setPromptText("Channel Password");

		ChannelBox.setSpacing(10);
		ChannelBox.setPadding(new Insets(15, 12, 0, 12));
		ChannelBox.getChildren().addAll(SwitchChannel, ChannelField, ChannelPasswordField);



		TopOrgan.getChildren().addAll(TopKey, ChannelBox, TopBoxes);

		border.setTop(TopOrgan);


		// Global

		Scene s = new Scene(border, 900, 700);
		s.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ESCAPE) {
					DontSend.setSelected(true);
				}else if(ke.getCode() == KeyCode.ALT_GRAPH){
					String[] xn = {".c"};
					ConsoleCommandParser.parse(xn);
				}/*else if(ke.getCode() == KeyCode.CONTROL){
					String[] xn = {".ACA"};
					ConsoleCommandParser.parse(xn);
				}*/
			}
		});


		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if(arg2){
					Main.primstage.getIcons().add(new Image("blue.png"));
				}
			}
		});


		Timeline tf = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				AddToMessageField("TPvtChat-C Release (1.0) | Type .help for informations", 2);
				AddToMessageField("Type: '.connect default' to connect to the Primary Server", 2);
				AddToMessageField(">ALT GR clear your chat<", 2);
				AddToConsoleField("[~] Console\n~~~~~~~~~~\n[~] Finished Init");
			}
		}));
		tf.play();


		primaryStage.setScene(s);
		primaryStage.setTitle("Private Messanger, Todays Topic: Do or Die");
		primaryStage.show();
	}

	public static TextField AddToMessageField(String s, int owner){
		if(!StyleChooser.isSelected() && owner == 2){
			if(s.startsWith(".System")){
				AlternativeAddToMessageField(s.replace(".System", ""), owner, ".System");
			}else{
				AlternativeAddToMessageField(s, owner, ".System");
			}
			return null;
		}
		HBox xn = createBubble(s, owner);
		xn.setId("b");
		content.getChildren().add(xn);

		for(int i=messagelist.length-1;i>-1;i--){
			if(i != messagelist.length-1 && i != 0){
				messagelist[i+1] = messagelist[i];
			}
			if(i == 0){
				messagelist[1] = messagelist[0];
				messagelist[0] = xn;
			}
			if(i == messagelist.length-1){
				HBox w = messagelist[messagelist.length-1];
				content.getChildren().remove(w);
			}
		}

		ObservableList<Node> obvn = xn.getChildren();
		TextField xp = null;
		for(Node xxn : obvn){
			xp = (TextField)xxn;
		}
		xp.setId("b");
		return xp;
	}

	public static void AlternativeAddToMessageField(String s, int owner, String Sender){
		HBox hnb = new HBox();

		if(Sender.length() < 16){
			for(int i=Sender.length(); i<=16; i++){
				//Sender = " " + Sender;
				Sender += " ";
			}
		}
		Text Usernamedisplay = new Text(Sender);
		Usernamedisplay.setId(Sender);
		Usernamedisplay.setFont(new Font("Courier New", 13));
		if(Sender.replace(" ", "").equals(ActiveUsername)){
			Usernamedisplay.setFill(Color.BLUE);
		}else if(Sender.replace(" ", "").equals(".System")){
			Usernamedisplay.setFill(Color.RED);
		}

		if(messagelist[0] != null){
			ObservableList<Node> hpp = messagelist[0].getChildren();
			for(Node node: hpp){
				try{
					if(node != null){
						if(node.getId() != null && !node.getId().equals("t") && !node.getId().equals("b") && !node.getId().endsWith("I")){
							Text f = (Text)node;
							if(f.getId().equals(Sender) && !Sender.replace(" ", "").equals(".System")){
								Usernamedisplay.setText("                 ");
							}
						}

					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		Text text2 = new Text(s);
		text2.setFill(Color.BLACK);
		text2.setFont(Font.font("Arial", 14));
		TextFlow tf = new TextFlow(text2);
		tf.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				HBox xp = hnb;
				for(int i = 0; i < messagelist.length; i++ ){
					if(messagelist[i] != null){
						if(messagelist[i].equals(xp)){
							content.getChildren().remove(messagelist[i]);
						}
					}
				}
			}
		});
		text2.setId("t");
		tf.setId("t");


		//ImageView stat = new ImageView();
		//stat.setId("I");

		Text timestamp = new Text(getTime());
		timestamp.setFont(new Font("Futura", 11));
		timestamp.setFill(Color.GREY);
		timestamp.setId("T");


		hnb.setSpacing(10);
		hnb.getChildren().addAll(timestamp, Usernamedisplay, tf/*, stat*/);

		hnb.setMaxWidth(content.getMaxWidth());
		hnb.setId("t");
		hnb.setAlignment(Pos.BASELINE_LEFT);
		content.getChildren().add(hnb);

		for(int i=messagelist.length-1;i>-1;i--){
			if(i != messagelist.length-1 && i != 0){
				messagelist[i+1] = messagelist[i];
			}
			if(i == 0){
				messagelist[1] = messagelist[0];
				messagelist[0] = hnb;
			}
			if(i == messagelist.length-1){
				HBox w = messagelist[messagelist.length-1];
				content.getChildren().remove(w);
			}
		}
	}

	@SuppressWarnings("unused")
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
		s = "<PENDING> "+s;
		boolean found = false;

		for(int i=0; i<messagelist.length; i++){
			if(messagelist[i] != null){
				ObservableList<Node> obvn = messagelist[i].getChildren();
				//boolean tn = false;
				for(Node xn : obvn){
					if(xn.getId() != null){

						/*if(tn){
							System.out.println("we did it");
							ImageView xny = (ImageView)xn;
							xny.setImage(new Image("sent.png"));
							tn = false;
						}*/

						if(xn.getId().equals("t")){
							TextFlow tff = (TextFlow)xn;
							ObservableList<Node> obvnn = tff.getChildren();
							for(Node n : obvnn){
								Text xnn = (Text)n;
								if(xnn.getText().equals(s)){
									String temp21 = s.substring(15, s.length());
									String text = temp21.substring(temp21.indexOf("]")+4, temp21.length());
									//xnn.setText(text);
									found = true;
									//tn = true;
									if(!DontSend.isSelected()){
										xnn.setText(text);
									}else{
										xnn.setText("<NOT SENT> " + text);
									}
								}
							}
						}else if(xn.getId().equals("b")){
							TextField fn = (TextField)xn;
							if(messagelist[i] != null){
								if(fn.getText().equals(s)){
									found = true;
									if(!DontSend.isSelected()){
										fn.setText(s.replace("<PENDING> text ", ""));
									}else{
										fn.setText(s.replace("<PENDING> text ", "<NOT SENT>"));
									}
								}
							}
						}
					}else{
						return true;
					}
				}
			}
		}
		if(found){
			return false;
		}
		return true;
	}

	public static void AddToConsoleField(String s){
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {
				console.appendText(s+"\n");
			}
		});
	}


	public static void DisconnectFromServer(){
		AddToConsoleField("[-] Disconnected from Server");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				names.remove(0, names.size());
			}
		});

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
				}else if(item.startsWith("<F>")){
					this.setTextFill(Color.GREEN);
					setText(item.substring(3, item.length()));
				}else{
					setText(item);
				}
			}
		}
	}

	public static void ChangeCellColor(int whattodo, String username){
		if(whattodo == 1){
			onlineusers.getItems().contains(username);
			ObservableList<String> n = onlineusers.getItems();
			n.removeAll(username);
			n.add("<F>"+username);
		}
		onlineusers.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> arg0) {
				return new ColorRectCell();
			}
		});
	}

	public static void SwitchChannel(String ChannelName, String Password){
		content.getChildren().clear();
		AddToMessageField(".Changing Channel", 0);
		names.remove(0, names.size());
		Client.processMessage(".channel " + ChannelName + " " + Password);
	}


	public static HBox createBubble(String txt, int owner){
		TextField textf = new TextField(txt);
		textf.setEditable(false);
		if(owner == 0){
			textf.setStyle("-fx-border-color: Green; -fx-border-width: 2px ;");
			textf.setAlignment(Pos.BASELINE_LEFT);
		}else if(owner == 1){
			textf.setStyle("-fx-border-color: Brown; -fx-border-width: 2px ;");	
			textf.setAlignment(Pos.BASELINE_RIGHT);
		}else if(owner == 2){
			textf.setStyle("-fx-border-color: Brown; -fx-border-width: 2px ;");	
			textf.setAlignment(Pos.BASELINE_CENTER);
		}

		textf.setFont(Font.font("Futura", 15));
		textf.setPrefWidth(content.getPrefWidth()-25);
		textf.setMaxWidth(content.getMaxWidth()-25);

		HBox hfn = new HBox(textf);
		hfn.setPrefWidth(content.getPrefWidth()-25);
		hfn.setMaxWidth(content.getMaxWidth()-25);

		return hfn;
	}

	public static String getTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		return time;
	}

	public static void ClearMsgField(){
		Main.content.getChildren().clear();
		for(int i = 0; i<messagelist.length; i++){
			messagelist[i] = null;
		}
	}

	public static void setUsername(){
		if(Username.getText().length() > 2 && !(Username.getText().contains(" ") || Username.getText().contains(".") || Username.getText().contains("+"))){
			if(Client.IsConnectedToServer){
				Client.processMessage(".namechange " + ActiveUsername + " " + Username.getText());
			}
			ActiveUsername = Username.getText();
			Username.setStyle("-fx-border-color: green; -fx-border-width: 2px ; -fx-text-fill: green");
			Timeline aftertickz = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					Username.setStyle("");
				}
			}));
			aftertickz.play();
			AddToConsoleField("[+] Changed Username");
		}else{
			AddToConsoleField("[-] Failed to change Username");
			Username.setStyle("-fx-border-color: red; -fx-border-width: 2px ; -fx-text-fill: red");
			Timeline aftertickz = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					Username.setStyle("");
				}
			}));
			aftertickz.play();

			Username.setText(Main.ActiveUsername);
		}
	}

}
