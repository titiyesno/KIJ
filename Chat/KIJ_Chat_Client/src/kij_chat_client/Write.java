/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
                                    System.out.println("Yang dikirim ke server: " + siapkirim);
                                    out.println(siapkirim);
                                    //out.println(input);
                                    out.flush();
                                    break;
                                case "pm":
                                    break;
                                default:
                                    out.println(input);//SEND IT TO THE SERVER
                                    out.flush();//FLUSH THE STREAM
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
            System.out.println("Key: " + Key);
            CTxt = CTxt + " " + Key;
            return CTxt;
            
        }

}
