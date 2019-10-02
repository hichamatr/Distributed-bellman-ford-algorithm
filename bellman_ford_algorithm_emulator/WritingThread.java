package bellman_ford_algorithm_emulator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class WritingThread extends Thread{
	
	private static final String lockCopy="LockCopy" ; 
	private transient Socket socket ; //transient to solve the problem of Serialization of socket Object
	private ObjectOutputStream outputStream ;
	Message routeTable ; 
	
	public WritingThread(Socket socket , Message routeTable) throws IOException, NullPointerException
	{
		this.socket = socket ;
		this.routeTable = routeTable ;
		outputStream = new ObjectOutputStream(this.socket.getOutputStream()) ;
	}
	
	//this makeCopy function solves a SerialiZation problem => it is important for updating data in the receiving side
	//more details on : https://stackoverflow.com/questions/12341086/java-socket-serialization-object-wont-update
	public synchronized Message makeCopy(Message msg)
	{
		HashMap<String, Integer> vect = new HashMap<>() ;
		Message copy = new Message(msg.getSource(), vect) ;
		
		for (String item : msg.getVectorTable().keySet())
		{
			vect.put(item, msg.getVectorTable().get(item)) ;
		}
		
		return copy ;
	}
	
	public void run()
	{
		try {
			while (true)
			{
				
				Message copyRouteTable = null ;
				
				//get a copy of the route table -- important step
				copyRouteTable = makeCopy(routeTable) ;
				
				
				//send this copy over the network
				outputStream.writeObject(copyRouteTable);
				//System.out.println("(*)(*)(*)I am "+copyRouteTable.getSource() +" i sent !! == "+copyRouteTable.getVectorTable());
				
				//sent every 300 ms
				try {Thread.sleep(300);}
				catch (InterruptedException e) 
				{e.printStackTrace();}
			}
			
		} 
		catch (IOException e) {e.printStackTrace();}
	}
	
			
			
			

}
