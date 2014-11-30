package me.Totenfluch.pack;

import java.util.Hashtable;

import me.Christian.networking.Client;
public class ConsoleCommandParser {
	public static Hashtable<String, String> UserTable = new Hashtable<String, String>();

	public static String[] Commands = {".help", ".connect <IP> <Port>", ".channel <Channelname> <ChannelPw>" , ".disconnect", ".admin", ".friend", ".clear"};
	public static void parse(String[] Args){
		if(Args[0].equals(".help")){
			Main.AddToMessageField(".System Available Commands:", true);
			for(int i = 0; i<Commands.length; i++){
				Main.AddToMessageField("	" + Commands[i], true);
			}
			Main.text.setText("");
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
			Main.text.setText("");
		}else if(Args[0].equals(".disconnect")){
			Main.content.getChildren().clear();
			Main.DisconnectFromServer();
			Main.text.setText("");
		}else if(Args[0].equals(".admin")){
			Client.processMessage(".admin");
			Main.text.setText("");
		}else if(Args[0].equals(".friend")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Main.AddToMessageField(".System Added " + temp + " as friend.", true);
			Main.ChangeCellColor(1, temp);
			UserTable.remove(temp);
			UserTable.put(temp, "Friend");
			Main.text.setText("");
		}else if(Args[0].equals(".kick")){
			String temp = Main.onlineusers.getSelectionModel().getSelectedItem();
			Client.processMessage(".kick " + temp);
		}else if(Args[0].equals(".clear")){
			Main.content.getChildren().clear();
			Main.text.clear();
		}else if(Args[0].equals(".channel")){
			if(Args.length > 1){
				Main.SwitchChannel(Args[1], "");
			}else if(Args.length > 2){
				Main.SwitchChannel(Args[1], Args[2]);
			}
		}else{
			Main.AddToConsoleField("[-] Invalid command");
		}
	}
}
