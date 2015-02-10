package me.Totenfluch.pack;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import me.Christian.networking.Client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Crypter {
	private static byte[] thekey = null;
	private static byte[] the2ndkey = null;
	private static byte[] the3rdkey = null;
	private static byte[] the4thkey = null;

	private static String hashit(String string){
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(string.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			final String result = new String(Hex.encodeHex(resultByte));
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	private static byte[] convertToByteString(String s){
		byte[] mainkey = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		int counter = 0;
		for(int i=0; i<32; i=i+2){
			mainkey[counter] = (byte)s.charAt(i);
			counter++;
		}
		return mainkey;
	}
	
	private static byte[] convertToByteString2(String s){
		byte[] mainkey = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		int counter = 0;
		for(int i=1; i<32; i=i+2){
			mainkey[counter] = (byte)s.charAt(i);
			counter++;
		}
		return mainkey;
	}
	
	public static void createKeys(){
		thekey = Crypter.convertToByteString(hashit(Main.Keyfield.getText()));
		the2ndkey = Crypter.convertToByteString2(hashit(Main.Keyfield.getText()));
		the3rdkey = Crypter.convertToByteString(hashit(Main.Keyfield.getText().toLowerCase().substring(32, 96)));
		the4thkey = Crypter.convertToByteString2(hashit(Main.Keyfield.getText().toUpperCase().substring(16, 72)));
	}

	private static String encrypt(String strToEncrypt, int ikey){
		byte[] key = null;
		if(ikey == 0){
			key = thekey;
			if(thekey == null){
				return "ERROR: NO KEY";
			}
		}else if(ikey == 1){
			key = the2ndkey;
			if(the2ndkey == null){
				return "ERROR: NO KEY2";
			}
		}else if(ikey == 2){
			key = the3rdkey;
			if(the3rdkey == null){
				return "ERROR: NO KEY3";
			}
		}else if(ikey == 3){
			key = the4thkey;
			if(the4thkey == null){
				return "ERROR: NO KEY4";
			}
		}else{
			return "Invalid Key";
		}
		try{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, int ikey){
		byte[] key = null;
		if(ikey == 0){
			key = thekey;
			if(thekey == null){
				return "ERROR: NO KEY";
			}
		}else if(ikey == 1){
			key = the2ndkey;
			if(the2ndkey == null){
				return "ERROR: NO KEY2";
			}
		}else if(ikey == 2){
			key = the3rdkey;
			if(the3rdkey == null){
				return "ERROR: NO KEY3";
			}
		}else if(ikey == 3){
			key = the4thkey;
			if(the4thkey == null){
				return "ERROR: NO KEY4";
			}
		}else{
			return "Invalid Key";
		}
		try{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.AddToConsoleField("[~] Wrong Key.");	
				}
			});
			return "Your String is not Encrypted!"+ikey;
		}
	}

	public static boolean doYourThing(String msg){
		if(thekey != null && the2ndkey != null && the3rdkey != null && the4thkey != null){
			msg = encrypt(encrypt(encrypt(encrypt(msg, 3), 2), 1), 0);
			final String x = msg;
			if(!Main.MessageSendDelayField.getText().equals("")){
				Timeline aftertick = new Timeline(new KeyFrame(Duration.millis(Integer.valueOf(Main.MessageSendDelayField.getText())), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if(!Main.DontSend.isSelected()){
							Client.processMessage(x);
						}else{
							String n = decrypt(decrypt(decrypt(decrypt(x, 0), 1), 2), 3);
							Main.RemoveFromMessageField(n);
							Main.AddToConsoleField("[!] Prevented |><|" + n + " |><| from beeing sent out.");
						}
					}
				}));
				aftertick.play();
			}else{
				Client.processMessage(msg);
			}
			return true;
		}else{
			Main.AddToMessageField(".System Message rejected. Use a Key!", 2);
			return false;
		}
	}


}
