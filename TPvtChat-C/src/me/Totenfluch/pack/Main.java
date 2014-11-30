package me.Totenfluch.pack;



import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import me.Christian.networking.Client;
import javafx.scene.control.CheckBox;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import javafx.scene.text.TextBoundsType;
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
	public static Stage primstage;
	public static TextField text;
	public static TextArea console;
	public static ScrollPane messageSP;
	public static ListView<String> onlineusers;
	public static TextField Keyfield, Username, MessageSendDelayField, ChannelField, ChannelPasswordField;
	public static CheckBox DontSend;
	public static Button Keylock;
	public static Text KeyAmount;
	public String ActiveFont = "Futura";
	public int ActiveFontSize = 20;
	public static VBox content;
	public static final ObservableList<String> names = 
			FXCollections.observableArrayList();

	public static PathTransition OptionBoxFlyIn = new PathTransition();

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
							AddToMessageField("<PENDING> " + Text, true);
							Crypter.doYourThing(Text);
							text.setText("");
						}else{
							AddToMessageField("[Local] " + Text, true);
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
		
		
		messageSP = new ScrollPane();
		messageSP.setPrefWidth(500);
		messageSP.setPadding(new Insets(0, 2, 2, 10));
		messageSP.setStyle("-fx-background: transparent");
		messageSP.setStyle("-fx-background-color:transparent;");
		
		content = new VBox();
		content.setPadding(new Insets(5, 5, 5, 10));
		content.setSpacing(15);
		content.setAlignment(Pos.TOP_LEFT);
		content.setPrefWidth(445);
		
		messageSP.setContent(content);
		
		
		console = new TextArea();
		console.setPrefWidth(200);
		console.setStyle("-fx-background-color: black;");
		console.setEditable(false);
		console.setWrapText(false);
		
		content.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				messageSP.setVvalue(messageSP.getVmax());
			}
		});
		
		centerfield.setPadding(new Insets(15, 12, 15, 12));
		centerfield.setSpacing(10);
		Button OpenOptions = new Button("<");
		OpenOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				OptionBoxFlyIn.play();
			}
		});
		OpenOptions.setPrefWidth(50);
		OpenOptions.setPrefHeight(400);
		centerfield.getChildren().addAll(OpenOptions, messageSP, console);


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
		TopKey.setSpacing(10);
		TopKey.getChildren().addAll(Keylock, Keyfield, KeyAmount);
		TopKey.setPadding(new Insets(15, 12, 0, 12));

		HBox TopBoxes = new HBox();
		Button UsernameRefresh = new Button("Use");
		UsernameRefresh.setPrefSize(75, 20);
		UsernameRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(Username.getText().length() > 2 && !(Username.getText().startsWith(" ") || Username.getText().startsWith(".") || Username.getText().startsWith("+"))){
					if(Client.IsConnectedToServer){
						Client.processMessage(".namechange " + ActiveUsername + " " + Username.getText());
					}
					ActiveUsername = Username.getText();
					AddToConsoleField("[+] Changed Username");
				}else{
					AddToConsoleField("[-] Failed to change Username");
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
				((Labeled) content.getChildren()).setFont(new Font(ActiveFont, ActiveFontSize));
			}
		});

		final ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(Color.BLACK);

		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				content.setStyle("-fx-background-color: Purple; -fx-text-fill: "+ toRgbString(colorPicker.getValue()) + ";");
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
				FontChooser.getSelectionModel().select(Integer.valueOf(new_val.toString()));
				ActiveFont = FontChooser.getSelectionModel().getSelectedItem();
				((Labeled) content.getChildren()).setFont(new Font(ActiveFont, ActiveFontSize));
			}
		});

		Text MessageSendDelayText = new Text();
		MessageSendDelayText.setText("Message send\ndelay in ms");
		MessageSendDelayText.setFont(Font.font("Futura", FontWeight.BOLD, 12));
		MessageSendDelayText.setFill(Color.LIME);

		MessageSendDelayField = new TextField("");
		MessageSendDelayField.setPromptText("1000 = 1s");
		MessageSendDelayField.setPrefWidth(115);
		MessageSendDelayField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(!arg2.matches("[Z0-9]+") && !arg2.equals("")){
					MessageSendDelayField.setText(arg1);
				}
			}
		});

		DontSend = new CheckBox("DONT SEND");
		DontSend.setFont(Font.font("Arial", 7));

		DontSend.setStyle("-fx-background-color:red;");

		DontSend.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				if(new_val){
					border.setStyle("-fx-background-color: RED");
					Main.AddToConsoleField("[!] Blocking ALL <PENDING> Messages");
				}else{
					border.setStyle("-fx-background-image: url('lol.jpg'); -fx-background-position: center center; -fx-background-size: 900 600;");
					Main.AddToConsoleField("[!] Allowing ALL <PENDING> Messages");
				}
			}
		});
		border.setStyle("-fx-background-image: url('lol.jpg'); -fx-background-position: center center; -fx-background-size: 900 600;");





		TopBoxes.getChildren().addAll(UsernameRefresh, Username, FontSize, colorPicker, FontChooser, MessageSendDelayText, MessageSendDelayField, DontSend);

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


		// Optionswindow

		/*VBox OptionsBox = new VBox();
		Label ServerCredicals = new Label("Server Credicals");
		TextField ServerIP = new TextField();
		TextField ServerPort = new TextField();
		TextField ServerPassword = new TextField();

		OptionsBox.setLayoutX(-500);
		OptionsBox.setLayoutY(0);

		OptionsBox.getChildren().addAll(ServerCredicals, ServerIP, ServerPort, ServerPassword);
		border.getChildren().add(OptionsBox);

		Path path = new Path();
		path.getElements().add (new MoveTo (-500, 0));
		path.getElements().add (new LineTo(200, 200));
		path.setStroke(Color.BLACK);
		border.getChildren().add(path);


		OptionBoxFlyIn.setDuration(Duration.millis(5000));
		OptionBoxFlyIn.setNode(OptionsBox);
		OptionBoxFlyIn.setPath(path);
		OptionBoxFlyIn.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		OptionBoxFlyIn.setAutoReverse(true);*/




		// Global

		Scene s = new Scene(border, 900, 500);
		s.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ESCAPE) {
					DontSend.setSelected(true);
				}
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

		primaryStage.setScene(s);
		primaryStage.setTitle("Private Messanger, Todays Topic: Do or Die");
		primaryStage.show();
	}

	public static void AddToMessageField(String s, boolean own){
		content.getChildren().add(createBubble(s, own));
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
		if(!DontSend.isSelected()){
			l = l.replace(s, s.replace("<PENDING> text ", ""));
		}else{
			l = l.replace(s, s.replace("<PENDING> text ", "<NOT SENT>"));
		}
		try{
			if(l.length() > 600){
				l = l.substring(l.length()-600, l.length());
			}
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
		AddToMessageField(".Changing Channel", true);
		names.remove(0, names.size());
		Client.processMessage(".channel " + ChannelName + " " + Password);
	}
	
	
	public static TextField createBubble(String txt, Boolean own){
		TextField textf = new TextField(txt);
		textf.setEditable(false);
		if(own){
			textf.setStyle("-fx-border-color: Green;");
			textf.setAlignment(Pos.BASELINE_LEFT);
		}else{
			textf.setStyle("-fx-border-color: Brown;");	
			textf.setAlignment(Pos.BASELINE_RIGHT);
		}
		textf.setPrefWidth(computeTextWidth(textf.getFont(),
				textf.getText(), 0.0D) + 10);
		textf.setFont(Font.font("Futura", 15));
		if(textf.getPrefWidth() > content.getPrefWidth()){
			textf.setPrefWidth(content.getPrefWidth()-10);
			textf.setPadding(new Insets(15, 15, 15, 15));
			double n = 15;
			while(computeTextWidth(textf.getFont(), textf.getText(), 0.0D)+50 > content.getPrefWidth() && n > 11){
				n -= 0.25;
				textf.setFont(Font.font("Futura", n));
			}
		}else{
			textf.setPadding(new Insets(10, 10, 10, 10));
		}
	
		return textf;
	}
	
	static final Text helper;
	static final double DEFAULT_WRAPPING_WIDTH;
	static final double DEFAULT_LINE_SPACING;
	static final String DEFAULT_TEXT;
	static final TextBoundsType DEFAULT_BOUNDS_TYPE;
	static {
	    helper = new Text();
	    DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
	    DEFAULT_LINE_SPACING = helper.getLineSpacing();
	    DEFAULT_TEXT = helper.getText();
	    DEFAULT_BOUNDS_TYPE = helper.getBoundsType();
	}

	public static double computeTextWidth(Font font, String text, double help0) {
	    // Toolkit.getToolkit().getFontLoader().computeStringWidth(field.getText(),
	    // field.getFont());

	    helper.setText(text);
	    helper.setFont(font);

	    helper.setWrappingWidth(0.0D);
	    helper.setLineSpacing(0.0D);
	    double d = Math.min(helper.prefWidth(-1.0D), help0);
	    helper.setWrappingWidth((int) Math.ceil(d));
	    d = Math.ceil(helper.getLayoutBounds().getWidth());

	    helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
	    helper.setLineSpacing(DEFAULT_LINE_SPACING);
	    helper.setText(DEFAULT_TEXT);
	    return d;
	}
}
