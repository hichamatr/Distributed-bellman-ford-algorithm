package bellman_ford_algorithm_emulator;

/**
 * @author : Hicham Elhaloumi
 * @description : ReadingThread, it is the Thread associated to listen the messages sent by the other END.
 * @date : (last modification) 29/09/2019
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class ReadingThread extends Thread {
	
	private final static String updateLock = "UpdateLock" ;
	
	private transient Socket socket ; //transient to solve the problem of Serialization of socket Object
	private ObjectInputStream inputStream ;
	Message routeTable ; //the route table to be modified 
	private HashMap<String, Integer> linkCosts = new HashMap<>() ;
	private HashMap<String, String> gateways = new HashMap<>() ;
	
	//constructor
	public ReadingThread(Socket socket , Message routeTable , HashMap<String, Integer> linkCosts , HashMap<String, String> gateways) throws IOException
	{
		this.socket = socket ;
		this.routeTable = routeTable ;
		this.linkCosts = linkCosts ;
		this.gateways = gateways ;
		inputStream = new ObjectInputStream(this.socket.getInputStream()) ;
		
	}
	
	//make copy
	public Message makeCopy(Message msg)
	{
		HashMap<String, Integer> vect = new HashMap<>() ;
		Message copy = new Message(msg.getSource(), vect) ;
		
		for (String item : msg.getVectorTable().keySet())
		{
			vect.put(item, msg.getVectorTable().get(item)) ;
		}
		
		return copy ;
	}
	
	//test if the new message is the same as the previous one ==> if it is the same we do not have to update
	public boolean sameAsPrevious(Message message)
	{
		boolean sameAsPrev = true ;
		
		if ( message.getVectorTable().size() == routeTable.getVectorTable().size())
		{
			for( String item : message.getVectorTable().keySet() )
			{
				if ( !routeTable.getVectorTable().containsKey(item))
				{
					sameAsPrev = false ;
					break ;
				}
				else
				{
					if ( routeTable.getVectorTable().get(item) != message.getVectorTable().get(item))
					{
						sameAsPrev = false ;
						break ;
					}
				}
			}
		}
		
		return sameAsPrev ;
	}
	
	//main reading thread 
	public void run()
	{
		Message message = null;
		try {

			while (true)
			{
				message = (Message)inputStream.readObject() ;				 
				
				 //If ok, then update the route table
				 synchronized (updateLock) {
					update(message);
				 }
				 
				 //read every 200 ms
				 try {Thread.sleep(300);}
					catch (InterruptedException e) 
					{e.printStackTrace();}
			 
				 System.out.println("\n<"+ routeTable.getSource()+ " Node:> Updated Vector Table: " + routeTable.getVectorTable() + "; =>Target Gateway: "+ gateways);
							 
			}
			
		} catch (ClassNotFoundException e) {e.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();}	
	}
	
	
	public Message receiveUpdatedRoutingTable() throws ClassNotFoundException, IOException 
	{			
		return routeTable;
	}
	
	/**Processing*/
	//update the routing table of the actual node ++ bellman ford algorithm
	public void update(Message message)
	{		
		for (String entryTable : message.getVectorTable().keySet())//get the keys of the table -- node's name
		{	
			if (!routeTable.getVectorTable().containsKey(entryTable) ) //the entry is new 
			{
				if(linkCosts.containsKey(entryTable)) //if it is adjacent
				{
					routeTable.getVectorTable().put(entryTable, linkCosts.get(entryTable)) ;
					gateways.put(entryTable, entryTable) ; // X ,X
				}
				else//else => initialize to infinity
				{
					routeTable.getVectorTable().put(entryTable, 999999) ; //we assume the the cost will never attempt 999999 (equivalent to infinity in the algorithm)
				}

			}
			else //the entry is already exists! -- => we are going to update if it is needed
			{
				Integer bellmanOpt ;
				
				bellmanOpt = message.getVectorTable().get(entryTable) + linkCosts.get(message.getSource()) ;
				
				if ( routeTable.getVectorTable().get(entryTable) > bellmanOpt )
				{
					routeTable.getVectorTable().put(entryTable, bellmanOpt) ;
					gateways.put(entryTable, message.getSource()) ;
				}
				
				//else: do nothing
			}
			
		}
			
		//System.out.println("\t\t\n=> from (issue de) "+ routeTable.getSource() + "\n"+routeTable.getVectorTable()+"> update route table"); 
		//isUpdate=false ; //reset the update flag 
	}

}

