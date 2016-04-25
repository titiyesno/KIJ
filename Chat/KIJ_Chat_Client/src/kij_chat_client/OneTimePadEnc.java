/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.util.Random;

/**
 *
 * @author tities
 */
public class OneTimePadEnc implements Runnable {
    String Msg;
    //String Psn;
    String Key = "";
    String CTxt = "";

    public OneTimePadEnc(String Msg) {
        // TODO code application logic here
        this.Msg = Msg;
        //System.out.println(Msg);
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try{
            
            
            //BufferedReader ObjIn = new BufferedReader(new InputStreamReader(System.in));
            //System.out.print("Enter the Message: \n");
            //Psn = ObjIn.readLine();
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
            System.out.println("Cip: " + CTxt);
            
        }
        
        catch(Exception err){
            System.err.println("Error: " + err);
        }
    }
}
