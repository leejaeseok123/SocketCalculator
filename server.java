package myPackage;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

    private static double calc(int opt, double x, double y){
        switch (opt) { //Performing an operation
            case 1: return x + y;
            case 2: return x - y;
            case 3: return x * y;
            case 4:
                if(y == 0){
                    throw new ArithmeticException("You can't divide it by zero");
                }
                return x / y;
            case 5:
                if(y == 0){
                	throw new ArithmeticException("You can't divide it by zero");
                }
                return x % y;
            case 6: return Math.pow(x, y);
            case 7:
                return 0;
            default:
            	 throw new IllegalArgumentException("Invalid operation code");
        }

    }

    
    private static class newClient implements Runnable{ //Client Request Processing Class
        private Socket socket;

        public newClient(Socket socket){ //Create a socket associated with a new client
            this.socket = socket;
        }

        public void run(){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while(true){
                    String str = br.readLine(); //Receive requests sent by clients
                    
                    if(str==null) break;
                    if(str.equalsIgnoreCase("finish")) { //Send a termination message to the client
                    	bw.write("Program is over\r\n");
                    	bw.flush();
                    	break;
                    }

                    StringTokenizer st = new StringTokenizer(str," ");
                    if(st.countTokens()!=3){
                        return;
                    }

                    int opt = Integer.parseInt(st.nextToken());
                    double x = Double.parseDouble(st.nextToken());
                    double y = Double.parseDouble(st.nextToken());

                    try {
                        double result = calc(opt, x, y);
                        bw.write("Result is: " + result + "\n");
                    } catch (ArithmeticException e) { //Dividing by 0
                        bw.write("ERROR: " + e.getMessage() + "\n");
                    } catch (IllegalArgumentException e) { //The case that you enter an invalid number
                        bw.write("ERROR: " + e.getMessage() + "\n");
                    }
                    bw.flush();

                }

            } catch(Exception e){
                System.out.println(e.getMessage());
            } finally{
                try {
                	if(socket!=null) socket.close();
                } catch(IOException e) {
                	System.out.println(e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) throws IOException{
        ExecutorService pool = Executors.newFixedThreadPool(5); //Up to five client parallel processing capabilities
        Socket socket = null;
        ServerSocket listener = null;
        
        try{
            listener = new ServerSocket(3622); //Create a server socket
            System.out.println("Waiting to connect");

            while(true){
                socket = listener.accept(); //Accept client's connection
                System.out.println("New client connection successful");
                pool.execute(new newClient(socket)); //Forward client processing tasks to thread pools
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        } finally{
            if(socket!=null) socket.close();
            if(listener!=null) listener.close();
        } 
    }

}