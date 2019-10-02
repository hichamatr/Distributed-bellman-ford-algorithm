package bellman_ford_algorithm_emulator;

/**
 * @author : Hicham Elhaloumi
 * @description : ClientThread, it represents the client END in client-server communication.
 * @date : (last modification) 29/09/2019
 */
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread{
	
	private Node client ;
	private Node server ;
	private int  port ;
	private Socket socket = null ;
	
	public void run()
	{
		 boolean serverIsReady = false ;
	     
	     while(!serverIsReady)//while the server is not ready, try to connect
	     {
		     try 
		     {
		    	 connectAsClientWithServer();
		    	 serverIsReady = true ; //connection is Ok!
		     } 
		     catch (ConnectException e) {serverIsReady = false ; e.printStackTrace();}
		     catch (UnknownHostException e) {e.printStackTrace();}  														   
		     catch (IOException e) {e.printStackTrace();} 
	     }
	     
		while(true) ; //keep the terminal alive
	}
	
	public ClientThread(Node client, Node server, int port)
	{
		this.client = client ;
		this.server = server ;
		this.port   = port ; 
	}
	
	public void connectAsClientWithServer() throws UnknownHostException, IOException, ConnectException
	{
		
		socket = new Socket(server.getAdress() , port) ;
		
		System.out.println("["+client.getNName()+"] As A Client> Connection esablished with the server : <"+server.getNName() +">.\n");
		
		//add this socket to the list of clients sockets
		//client.terminationSockets.put(String.valueOf(port), socket) ;
		
	}
	
	//get the client socket 
	public Socket getSocket()
	{
		return socket ;
	}

}
