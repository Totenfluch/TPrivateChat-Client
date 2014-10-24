package me.Christian.networking;

import me.Totenfluch.pack.ConsoleCommandParser;
import me.Totenfluch.pack.Crypter;
import me.Totenfluch.pack.Main;
import javafx.application.Platform;


public class GetServerMessages{
	public static String newestreply = null;


	public static void CheckServerMessages(String FullMsg){
		newestreply = FullMsg;
		String[] Args = FullMsg.split(" ");
		if(Args[0].equals(".System")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.AddToMessageField(FullMsg);
				}
			});
		}else if(Args[0].equals(".connect")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(!Main.names.contains(Args[1])){
						Main.names.add(Args[1]);
					}
					if(!ConsoleCommandParser.UserTable.contains(Args[1])){
						ConsoleCommandParser.UserTable.put(Args[1], "User");
					}
					//Main.ChangeCellColor(0, "x");
					Main.AddToMessageField(".System " + Args[1] + " connected.");
				}
			});
		}else if(Args[0].equals(".disconnect")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(Main.names.contains(Args[1])){
						Main.names.remove(Args[1]);
					}
					if(ConsoleCommandParser.UserTable.contains(Args[1])){
						ConsoleCommandParser.UserTable.remove(Args[1]);
					}
					//Main.ChangeCellColor(0, "x");
					Main.AddToMessageField(".System " + Args[1] + " disconnected.");
				}
			});
		}else if(Args[0].equals(".namechange")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(Main.names.contains(Args[1])){
						Main.names.remove(Args[1]);
						Main.names.add(Args[2]);
					}else{
						Main.names.add(Args[2]);
					}
					
					if(ConsoleCommandParser.UserTable.contains(Args[1])){
						ConsoleCommandParser.UserTable.remove(Args[1]);
						ConsoleCommandParser.UserTable.put(Args[2], "User");
					}else{
						ConsoleCommandParser.UserTable.put(Args[2], "User");
					}
					//Main.ChangeCellColor(0, "x");
					Main.AddToMessageField(".System " + Args[1] + " changed name to " + Args[2] + ".");
				}
			});
		}else if(Args[0].equals(".confirmAdmin")){
			Main.ActiveUsername = "."+Main.ActiveUsername;
		}else if(Crypter.decrypt(FullMsg, 0).startsWith("text")){
			String nonshit = Crypter.decrypt(FullMsg, 0);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(!Main.RemoveFromMessageField(nonshit)){
						Main.AddToMessageField(nonshit.replace("text ", ""));
					}
				}
			});
		}
	}
}
