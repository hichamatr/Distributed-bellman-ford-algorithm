package bellman_ford_algorithm_emulator;

/**
 * @author : Hicham Elhaloumi
 * @description : ServerThread, it is the Server ENd of a client-server communication.
 * @date : (last modification) 29/09/2019
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

	
	private Node node ;
	private int port ; 
	private Socket socket = null ;
	 
	@Override
	public void run() {
		
		try {runServer() ;} 
		catch (IOException e) {e.printStackTrace();} // run the server with the actual configuration
		while(true) ; //Do not forget this -- server should kept alive
	}
	
	private void runServer() throws IOException
	{
		ServerSocket serverSock = new ServerSocket(this.port) ;
		
		//wait until a connection arrives
		socket = serverSock.accept() ;
				
		//add the socket to the list of adjacent sockets
		//node.terminationSockets.put(String.valueOf(port), socket) ;
		
	}
	
	//get the client socket 
	public Socket getSocket()
	{
		return socket ;
	}
	
	//constructor
	public ServerThread(Node node, int port)
	{
		this.node = node ;
		this.port = port ;
	}
	
	//getters and setters
	public Node getNode()
	{
		return this.node ;
	}
	
	public void setNode(Node node)
	{
		this.node = node ;
	}
	
	
	public int getPort()
	{
		return this.port ;
	}
	
	public void setPort(int port)
	{
		this.port = port ;
	}

}
