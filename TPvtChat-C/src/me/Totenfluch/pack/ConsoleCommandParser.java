package me.Totenfluch.pack;

import java.util.Hashtable;

import me.Christian.networking.Client;
public class ConsoleCommandParser {
	public static Hashtable<String, String> UserTable = new Hashtable<String, String>();

	public static String[] Commands = {".help", ".connect <IP> <Port>", ".channel <Channelname> <ChannelPw>" , ".disconnect", ".admin", ".friend", ".clear", ".c", ".clearconsole", ".cc", ".clearall", ".ca"};
	public static void parse(String[] Args){
		if(Args[0].equals(".help")){
			Main.AddToMessageField(".System Available Commands:", 0);
			for(int i = 0; i<Commands.length; i++){
				Main.AddToMessageField("	" + Commands[i], 0);
			}
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".connect")){
			Main.content.getChildren().clear();
			if(Args.length > 2){
				if(Main.ConnectToServer(Args[1], Integer.valueOf(Args[2]))){
					Main.AddToConsoleField("[+] Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
				}else{
					Main.AddToConsoleField("[-] Failed to Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
				}
			}else{
				Main.AddToConsoleField("[-] Invalid Port OR IP");
			}
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".disconnect")){
			Main.content.getChildren().clear();
			Main.DisconnectFromServer();
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".admin")){
			Client.processMessage(".admin");
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".friend")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Main.AddToMessageField(".System Added " + temp + " as friend.", 0);
			Main.ChangeCellColor(1, temp);
			UserTable.remove(temp);
			UserTable.put(temp, "Friend");
			Main.TextInputField.setText("");
		}else if(Args[0].equals(".kick")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Client.processMessage(".kick " + temp);
		}else if(Args[0].equals(".clear") || Args[0].equals(".c")){
			Main.content.getChildren().clear();
			Main.TextInputField.clear();
		}else if(Args[0].equals(".clearconsole") || Args[0].equals(".cc")){
			Main.console.setText("");
		}else if(Args[0].equals(".ACA")){
			Main.TextInputField.clear();
			Client.processMessage(".ACA");
		}else if(Args[0].equals(".clearall") || Args[0].equals(".ca")){
			Main.console.setText("");
			Main.content.getChildren().clear();
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
					Main.messageSP.setPrefWidth(690);
					Main.content.setPrefWidth(635);
					Main.OpenOptions.setText("<");
				}else{
					Main.centerfield.getChildren().add(Main.console);
					Main.messageSP.setPrefWidth(500);
					Main.content.setPrefWidth(445);
					Main.OpenOptions.setText(">");
				}
				Main.TextInputField.setText("");
			}
		}else{
			Main.AddToConsoleField("[-] Invalid command");
		}
	}
}
