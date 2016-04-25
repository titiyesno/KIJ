/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.util.ArrayList;
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
                                    String msg = input.split(" ")[1];
                                    OneTimePadEnc enkrip = new OneTimePadEnc(msg);
                                    Thread enc = new Thread(enkrip);
                                    enc.start();
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

}
