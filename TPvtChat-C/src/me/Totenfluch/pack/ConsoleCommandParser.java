package me.Totenfluch.pack;

import me.Christian.networking.Client;

public class ConsoleCommandParser {
	public static String[] Commands = {".help", ".connect <IP> <Port>", ".disconnect", ".admin"};
	public static void parse(String[] Args){
		if(Args[0].equals(".help")){
				Main.AddToMessageField(".System Available Commands:");
			for(int i = 0; i<Commands.length; i++){
				Main.AddToMessageField("	" + Commands[i]);
			}
			Main.text.setText("");
		}else if(Args[0].equals(".connect")){
			if(Main.ConnectToServer(Args[1], Integer.valueOf(Args[2]))){
				Main.AddToConsoleField("[+] Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
			}else{
				Main.AddToConsoleField("[-] Failed to Connected to Server: '"  + Args[1] + ":" + Args[2] + "'");
			}
			Main.text.setText("");
		}else if(Args[0].equals(".disconnect")){
			Main.DisconnectFromServer();
			Main.text.setText("");
		}else if(Args[0].equals(".admin")){
			Client.processMessage(".admin");
			Main.text.setText("");
		}else{
			Main.AddToConsoleField("[-] Invalid command");
		}
	}
}
