package myPackage;
import java.io.*;
import java.util.*;

public class Config {
	private String IP;
	private int port;
	
	BufferedReader  br = null;
	BufferedWriter bw = null;
	
	public Config(String fileName) {
		try {
			File file = new File(fileName); //Create File Object
			if(!file.exists()) { //When the file does not exist
				System.out.println("The file does not exist. So create as default.");
				this.IP = "localhost"; //Default server IP
				this.port = 1234; //Default port number
				saveFile(file); //
			}
			else { //When a file exists
				readFile(file);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void readFile(File file) { //Function to read files
		try {
			br = new BufferedReader(new FileReader(file));
			this.IP = br.readLine();
			this.port = Integer.parseInt(br.readLine());
		} catch(Exception e) {
			System.out.println(e.getMessage());
			this.IP = "localhost";
			this.port = 1234;
		}
	}
	
	private void saveFile(File file) { //Functions that store files
		try {
			bw  = new BufferedWriter(new FileWriter(file));
			bw.write(IP+"\n");
			bw.write(port+"\n");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getIP() { //Function to import stored IP
		return IP;
	}
	
	public int getPort() { //Function to import stored port number
		return port;
	}
	
}
