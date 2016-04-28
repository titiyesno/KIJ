/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
	String dec;
        
	public Read(Scanner in, ArrayList<String> log)
	{
		this.in = in;
                this.log = log;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				if(this.in.hasNext()) {
                                                                   //IF THE SERVER SENT US SOMETHING
                                        input = this.in.nextLine();
                                        //System.out.println("Dari server: " + input);
                                        //System.out.println("Split: " + input.split(" ")[1]);
                                        if (input.split(" ")[0].toLowerCase().equals("success")) {
                                            System.out.println(input);//PRINT IT OUT
                                            if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                                keepGoing = false;
                                            } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                                                log.clear();
                                                log.add("true");
                                            }
                                        }
                                        else if(input.split(" ")[1].toLowerCase().equals("<broadcast>:")){
                                            //System.out.println("ini bm");
                                            String[] msg = input.split(" ");
                                            dec = AesCtrDec(msg[2],msg[3]);
                                            String pln = msg[0] + " " + msg[1] + " " + dec;
                                            System.out.println(pln);
                                        }
                                        else if(input.split(" ")[1].equals("@")){
                                            //System.out.println("ini bm");
                                            String[] msg = input.split(" ");
                                            dec = AesCtrDec(msg[4],msg[5]);
                                            String pln = msg[0] + " @ " + msg[2] + " " + msg[3] +" " + dec;
                                            System.out.println(pln);
                                        }
                                        else{
                                            String[] param = input.split(" ");
                                            //System.out.println(pesan+" "+key);
                                            
                                            dec = AesCtrDec(param[1],param[2]);
                                            String printout = param[0]+" "+dec;
                                            System.out.println(printout);
                                        }
                                        
                                }
                                
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
        
         public String AesCtrDec(String pesan, String key) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException
         {
            byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
             
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKey key2 = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            
            byte[] decodedMsg = Base64.getDecoder().decode(pesan);
            
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            //System.out.print(decodedKey);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key2, ivSpec);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
            cOut.write(decodedMsg);
            cOut.close();
            //System.out.println("plain : " + new String(bOut.toByteArray()));
            String plainText = new String(bOut.toByteArray());
            return plainText;
            //return key.toString();
         }
}
