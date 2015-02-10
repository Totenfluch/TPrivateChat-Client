package me.Totenfluch.pack;

import java.util.Hashtable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import me.Christian.networking.Client;
public class ConsoleCommandParser {
	public static Hashtable<String, String> UserTable = new Hashtable<String, String>();

	public static String[] Commands = {".help", ".connect <IP> <Port>", ".channel <Channelname> <ChannelPw>" , ".disconnect", ".admin", ".friend", ".clear", ".c", ".clearconsole", ".cc", ".clearall", ".ca", ".ACA - clear chat of everyone"};
	public static void parse(String[] Args){
		if(Args[0].equals(".help")){
			Main.AddToMessageField(".System Available Commands:", 2);
			for(int i = 0; i<Commands.length; i++){
				if(Main.StyleChooser.isSelected()){
					Main.AddToMessageField("	" + Commands[i], 0);
				}else{
					Main.AlternativeAddToMessageField("	" + Commands[i], 0, ".System");
				}
			}
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".connect")){
			Main.ClearMsgField();
			if(Args.length == 3){
				if(Main.ConnectToServer(Args[1], Integer.valueOf(Args[2]))){
					Main.AddToConsoleField("[+] Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
				}else{
					Main.AddToConsoleField("[-] Failed to Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
				}
			}else if(Args.length == 2){
				if(Args[1].equals("default")){
					if(Main.ConnectToServer("188.193.229.219", 1505)){
						Main.AddToConsoleField("[+] Connected to Server: 'Primary TChat Server'");
					}else{
						Main.AddToConsoleField("[-] Failed to Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
					}
				}
				Main.AddToConsoleField("");
			}else{
				Main.AddToConsoleField("[-] Invalid Port OR IP");
			}
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".disconnect")){
			Main.ClearMsgField();
			Main.DisconnectFromServer();
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".admin")){
			Client.processMessage(".admin");
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".friend")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Main.AddToMessageField(".System Added " + temp + " as friend.", 2);
			Main.ChangeCellColor(1, temp);
			UserTable.remove(temp);
			UserTable.put(temp, "Friend");
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".kick")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Client.processMessage(".kick " + temp);
			Main.TextInputField.clear();
		}else if(Args[0].equals(".clear") || Args[0].equals(".c")){
			Main.ClearMsgField();
			Main.TextInputField.clear();
		}else if(Args[0].equals(".clearconsole") || Args[0].equals(".cc")){
			Main.console.setText("");
			Main.TextInputField.clear();
		}else if(Args[0].equals(".ACA")){
			Main.TextInputField.clear();
			Client.processMessage(".ACA");
		}else if(Args[0].equals(".clearall") || Args[0].equals(".ca")){
			Main.console.setText("");
			Main.ClearMsgField();
			Main.TextInputField.clear();
		}else if(Args[0].equals(".channel")){
			if(Args.length > 1){
				Main.SwitchChannel(Args[1], "");
			}else if(Args.length > 2){
				Main.SwitchChannel(Args[1], Args[2]);
			}
		}else if(Args[0].equals(".toggle")){
			if(Args[1].equals("console")){
				if(Main.console.getParent() != null){
					Main.centerfield.getChildren().remove(Main.console);
					Main.OpenOptions.setText("<");
					Timeline fxn = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							Main.content.setMaxWidth(Main.primstage.getWidth()-268);
							Main.content.setPrefWidth(Main.primstage.getWidth()-268);
							for(int i = 0; i < Main.messagelist.length; i++){
								if(Main.messagelist[i] != null) {
									Main.messagelist[i].setPrefWidth(Main.content.getPrefWidth()-10);
									Main.messagelist[i].setMaxWidth(Main.content.getMaxWidth()-10);
								}
							}
						}
					}));
					fxn.play();
				}else{
					Main.centerfield.getChildren().add(Main.console);
					Main.OpenOptions.setText(">");
					Timeline fxn = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							Main.content.setMaxWidth(Main.primstage.getMaxWidth()-478);
							Main.content.setPrefWidth(Main.primstage.getWidth()-478);
							for(int i = 0; i < Main.messagelist.length; i++){
								if(Main.messagelist[i] != null) {
									Main.messagelist[i].setPrefWidth(Main.content.getPrefWidth()-10);
									Main.messagelist[i].setMaxWidth(Main.content.getMaxWidth()-10);
								}
							}
						}
					}));
					fxn.play();
				}
				Main.TextInputField.setText("");
			}
		}else{
			Main.AddToConsoleField("[-] Invalid command");
		}
	}
}
