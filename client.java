package myPackage;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
    public static void main(String[] args) throws IOException{
    	Config config = new Config("server_info.dat"); //Read IP and port number from file
    	String IP = config.getIP(); 
    	int port = config.getPort();
    	
    	Scanner sc = new Scanner(System.in);
        BufferedReader br = null;
        BufferedWriter bw = null;
        Socket socket = null; //Connection objects to the server

        try{
        	//Attempt to connect to the server
            socket = new Socket(IP,port); 
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while(true){ //client operation
                System.out.println("1. Add");
                System.out.println("2. Subtract");
                System.out.println("3. Multiply");
                System.out.println("4. Divide");
                System.out.println("5. Modulus");
                System.out.println("6. Power");
                System.out.println("7. Finish");
                System.out.print("Choice: ");
                int opt = sc.nextInt();

                if(opt == 7){ //Terminate when you enter finish
                    bw.write("finish\n");
                    bw.flush();
                    
                    String msg = br.readLine(); 
                    System.out.println(msg);
                    break;
                }
                
                //Enter two numbers
                System.out.print("Enter first number: ");
                double x = sc.nextDouble();
                System.out.print("Enter second number: ");
                double y = sc.nextDouble();

                bw.write(opt+" "+x+" "+y+"\n"); //Send operation request
                bw.flush();
                String result = br.readLine();
                System.out.println(result);
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        } finally{
            if(socket!=null) {
                socket.close();
            }
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
            sc.close();
        }
    }
    
}