package me.Christian.networking;

import java.io.*;
import java.net.*;

import me.Totenfluch.pack.Main;

public class Client implements Runnable
{
	public static boolean IsConnectedToServer = false;
	public static String LatestServerReply = "";
	public boolean waitingforreply = false;
	public boolean running = true;
	public Thread thread = null;

	public String format;
	public Socket socket;
	public DataOutputStream dout;
	public DataInputStream din;

	public Client( String host, int port) {

		try {
			socket = new Socket( host, port );
			din = new DataInputStream( socket.getInputStream() );
			dout = new DataOutputStream( socket.getOutputStream() );
			IsConnectedToServer = true;
			thread = new Thread( this );
			thread.start();
		}catch( IOException ie ){
			Main.DisconnectFromServer();
		}

		running = true;
	}


	public static void processMessage( String message ) {
			try {
				Main.connection.dout.writeUTF( message );
			} catch( Exception ie ){
				ie.printStackTrace();
				System.out.println( ie ); 
			}
		
	}

	public void run() {
		try {
			while (running) {
				if(IsConnectedToServer == true){
					String message = null;
					try{
						message = din.readUTF();
						LatestServerReply = message;
						GetServerMessages.CheckServerMessages(message);
						waitingforreply = false;
					}catch(Exception e){
						IsConnectedToServer = false;
						Main.DisconnectFromServer();
					}
				}
			}
		}catch( Exception ie ){
			ie.printStackTrace();
			IsConnectedToServer = false;
			running = false;
			Main.DisconnectFromServer();
		} finally {
			try{
				if(dout != null){
					dout.close();
				}
				if(din != null){
					din.close();
				}
				if(socket != null){
					socket.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}