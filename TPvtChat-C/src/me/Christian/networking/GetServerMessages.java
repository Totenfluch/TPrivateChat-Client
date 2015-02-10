package me.Christian.networking;

import me.Totenfluch.pack.ConsoleCommandParser;
import me.Totenfluch.pack.Crypter;
import me.Totenfluch.pack.Main;
import javafx.application.Platform;
import javafx.scene.image.Image;


public class GetServerMessages{
	public static String newestreply = null;


	public static void CheckServerMessages(String FullMsg){
		newestreply = FullMsg;
		String[] Args = FullMsg.split(" ");
		if(Args[0].equals(".System")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.AddToMessageField(FullMsg.replace(".System", ""), 2);
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
					Main.AddToMessageField(".System " + Args[1] + " connected.", 2);
					if(!Main.primstage.isFocused()){
						Main.primstage.getIcons().add(new Image("orange.png"));
					}
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
					Main.AddToMessageField(".System " + Args[1] + " disconnected.", 2);
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
					Main.AddToMessageField(".System " + Args[1] + " changed name to " + Args[2] + ".", 2);
				}
			});
		}else if(Args[0].equals(".confirmAdmin")){
			Main.ActiveUsername = "."+Main.ActiveUsername;
		}else if(Args[0].equals(".ACA")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.console.setText("");
					Main.ClearMsgField();
					Main.TextInputField.clear();
				}
			});
		}else if(Crypter.decrypt(Crypter.decrypt(Crypter.decrypt(Crypter.decrypt(FullMsg, 0), 1), 2), 3).startsWith("text")){
			String entrymsg = Crypter.decrypt(Crypter.decrypt(Crypter.decrypt(Crypter.decrypt(FullMsg, 0), 1), 2), 3);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(Main.RemoveFromMessageField(entrymsg)){
						if(Main.StyleChooser.isSelected()){
							Main.AddToMessageField(entrymsg.replace("text ", ""), 1);
						}else{
							String temp21 = entrymsg.substring(5, entrymsg.length());
							String Sender = temp21.substring(1, temp21.indexOf("]"));
							String text = temp21.substring(temp21.indexOf("]")+4, temp21.length());
							Main.AlternativeAddToMessageField(text, 0, Sender);
						}
						if(!Main.primstage.isFocused()){
							Main.primstage.getIcons().add(new Image("orange.png"));
						}
					}
				}
			});
		}else{

		}
	}
}
