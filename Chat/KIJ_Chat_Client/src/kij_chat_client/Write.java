/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
	private final Scanner chat;
        private final PrintWriter out;
        boolean keepGoing = true;
        ArrayList<String> log;
        String ciper;
        String siapkirim;
        String chp;
        String pesansiap;
	
	public Write(Scanner chat, PrintWriter out, ArrayList<String> log)
	{
		this.chat = chat;
                this.out = out;
                this.log = log;
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                            //String split = input.split(" ")[0];
                            //System.out.println("Split[0]: " + split);
                            switch (input.split(" ")[0].toLowerCase()) {
                                case "login":
                                //System.out.println("Ini login");
                                    //System.out.println(input.split(" ")[1]);
                                    String msg = input.split(" ")[2];
                                    ciper = OneTimePadEnc(msg);
                                    //System.out.println(ciper);
                                    siapkirim = input.split(" ")[0].toUpperCase() + " " + input.split(" ")[1] + " " + ciper;
                                    //System.out.println("Yang dikirim ke server: " + siapkirim);
                                    out.println(siapkirim);
                                    //out.println(input);
                                    out.flush();
                                    break;
                                case "pm":
                                    String pesan = input.split(" ")[2];
                                    chp = AesCtr(pesan);
                                    //System.out.println(chp);
                                    pesansiap = input.split(" ")[0].toUpperCase() + " " + input.split(" ")[1] + " " + chp;
                                    //System.out.println("Yang dikirim ke server: " + pesansiap);
                                    out.println(pesansiap);
                                    out.flush();
                                    break;
                            //out.println(input);//SEND IT TO THE SERVER
                            //out.flush();//FLUSH THE STREAM
                            }
                                
                                if (input.contains("logout")) {
                                    if (log.contains("true"))
                                        keepGoing = false;
                                    
                                }
			}
		}
		catch (Exception e)
		{
                    e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
        
        public String OneTimePadEnc(String Msg){
            //String Msg;
    //String Psn;
            String Key = "";
            String CTxt = "";
            Msg = Msg.toUpperCase();
            //System.out.println(Msg);
            
            Random randomGenerator = new Random();
            for(int idx = 1; idx <= Msg.length(); ++idx){
                int randomInt = randomGenerator.nextInt(26);
                Key = Key + (char)(65+randomInt);
            }
            //System.out.println("Key: \n" + Key);
            
            int temp;
            char c;
            int sum;
            for(int i = 0; i < Msg.length(); i++){
                sum = Msg.charAt(i) + Key.charAt(i);
                
                /*System.out.println("Sum: " + sum);
                System.out.println("Msg.chartAt(i): " + Msg.charAt(i));
                System.out.println("Key.chartAt(i): " + Key.charAt(i));*/
                
                if(sum >= 155){
                    temp = sum - 90;
                }
                else{
                    temp = sum - 64;
                }
                
                //System.out.println("temp: " + temp);
                
                
                c = (char) temp;
                
                //System.out.println("c: " + c);
                
                CTxt = CTxt + c;
            }
            //System.out.println("Key: " + Key);
            CTxt = CTxt + " " + Key;
            return CTxt;
            
        }

        public String AesCtr(String pesan) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException{
            //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
            //byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
            //byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
           
            //SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            //IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            System.out.println("input : " + new String(pesan));
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            SecretKey key = kg.generateKey();
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] pesanbyte = pesan.getBytes();
            ByteArrayInputStream bIn = new ByteArrayInputStream(pesanbyte);
            CipherInputStream cIn = new CipherInputStream(bIn, cipher);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            
            int ch;
            while ((ch = cIn.read()) >= 0) {
              bOut.write(ch);
            }

            byte[] cipherText = bOut.toByteArray();
            byte[] skey = key.getEncoded();
            
            System.out.println("cipher: " + new String(cipherText));
            String cipp = cipherText.toString()+" "+skey.toString();
            return cipp;
           }
        
}
