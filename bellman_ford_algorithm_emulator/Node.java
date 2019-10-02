package bellman_ford_algorithm_emulator;

/**
 * @author : Hicham Elhaloumi
 * @description : Node class, represents the structure of our nodes in the network
 * @date : (last modification) 29/09/2019
 */

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Node extends Thread implements Serializable
{
	//auto generated
	private static final long serialVersionUID = 1L;
	
	static final String  Lock = "myLock";
	static final String  LockSyncPoint = "myLockSync";
	
	private Message routingTable ;
	private String adress = "localhost" ;
	private String name = "" ;	
	
	private transient HashMap<String,Socket> terminationSockets = new HashMap<>(); //dictionary of all the active sockets -- port , Socket -- the field should not be a part of the serialization process
	private HashMap<String,Node> adjacentNodes = new HashMap<>() ; //string = "address" + "Port"
	private HashMap<String,Boolean> isServerWithAdjacent = new HashMap<>() ;//The state of the actual node with its adjacent node, is a client or server..
	private HashMap<String,Boolean> statusIsConnectedWithAdjacent = new HashMap<>() ;//Is already connected with the adjacent nodes..
	private HashMap<String,Node> portesAssociatedToAdjacents = new HashMap<>(); //all the port numbers of the actual node
	private HashMap<String,Integer> linkCosts = new HashMap<>() ; //name -- cost
	private HashMap<String, String> gateways = new HashMap<>() ;
	
	public Node(String name)
	{
		this.name = name ;
		//initialize the vector table at 0
		HashMap<String, Integer> vectorTable = new HashMap<String, Integer>() {{put(name , 0) ;}} ;
			
		routingTable = new Message(name, vectorTable);

	}
	
	//getters of the Class
	public HashMap<String, String> getGateWays()
	{
		return gateways ;
	}
	
	public HashMap<String,Socket> getTerminationSockets()
	{
		return terminationSockets ;
	}
	
	public HashMap<String,Integer> getLinkCosts()
	{
		return linkCosts ;
	}
	
	public HashMap<String,Node> getPortesAssociatedToAdjacents()
	{
		return portesAssociatedToAdjacents ;
	}
	
	public HashMap<String, Boolean> getStatusIsConnectedWithAdjacent()
	{
		return statusIsConnectedWithAdjacent ;
	}
	
	public HashMap<String,Boolean> getIsServerWithAdjacent()
	{
		return isServerWithAdjacent ;
	}
	
	public HashMap<String, Node> getAdjacentNodes()
	{
		return adjacentNodes ;
	}

	public String getAdress()
	{
		return adress ;
	}
	
	public String getNName() {
		return name;
	}

	public void setNName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Node=" + name;
	}
	
	public void describeNode()
	{
		
		System.out.println("(*)Node name = "+name+":\n\t=> Adjacent nodes :" + adjacentNodes +
				"\n\t=> Is Server with Adjacents : "+isServerWithAdjacent+
				"\n\t=> Status with Adjacents : "+statusIsConnectedWithAdjacent +
				"\n\t=> portes Associated to Adjacents : " + portesAssociatedToAdjacents +
				"\n\t------------------------------------------------------");
	}


	public void run()
	{
		synchronized (Lock) 
		{
			//System.out.println(name +" Got the Lock");
			//we synchronize this section be cause the threads modify the state of each others interchangeably
			for ( String isAServerItterator : isServerWithAdjacent.keySet())//address + port
			{
				if ( !statusIsConnectedWithAdjacent.get(isAServerItterator)  )//if not yet connected 
				{
					//get the port number
					StringTokenizer st = new StringTokenizer(isAServerItterator,"|");  
					String port = "" ;
				    while (st.hasMoreTokens())
				         port = st.nextToken() ; //get last item which represents the port
				     
				    //get the adjacent node reference of the actual extremity -- by port number
				    Node adjNode = portesAssociatedToAdjacents.get(port);
				     
				    //convert the string port to integer 
				    Integer portNumber = -1 ;
				    try{portNumber = Integer.valueOf(port) ;} 
				    catch (Exception e) {e.printStackTrace();}
				     
				   //i am a client
					if (!isServerWithAdjacent.get(isAServerItterator))
					{
					     //connect the adjNode node first as a server (wait for new connections) 
					     //try {adjNode.connectAsServer(portNumber); } 
					     //catch (IOException e) { e.printStackTrace(); } 
						ServerThread sthread = new ServerThread(adjNode, portNumber) ;
						sthread.start();
						
						//then, connect me to ADJNODE
						ClientThread cthread = new ClientThread(this, adjNode , portNumber) ;
						cthread.start();
						
						//wait notify is an other solution
						try {Thread.sleep(500);} 
						catch (InterruptedException e) {e.printStackTrace();}
						
						//then add the created extremities to the associated lists
						adjNode.terminationSockets.put(port, sthread.getSocket()) ;
						this.terminationSockets.put(port, cthread.getSocket()) ;
						 					     
					}
					else//i am a server
					{
						//connect me first as a server
						ServerThread sthread = new ServerThread(this, portNumber) ;
						sthread.start();

						//then, connect adjNode to this server
						ClientThread cthread = new ClientThread(adjNode, this, portNumber) ;
						cthread.start();
						
						try {Thread.sleep(500);} 
						catch (InterruptedException e) {e.printStackTrace();}

						//then add the created extremities to the associated lists
						this.terminationSockets.put(port, sthread.getSocket()) ;
						adjNode.terminationSockets.put(port, cthread.getSocket()) ;
	
					}
					
					//set the connectivity labels of the two extremities 
					statusIsConnectedWithAdjacent.put(isAServerItterator, true) ;
					adjNode.statusIsConnectedWithAdjacent.put(isAServerItterator, true);
				}
			}
			//System.out.println(name +" Frees the Lock");

		}//end of critical section
		
		/**"Synchronization point" implementation*/
		synchronized (LockSyncPoint) {
			if ( Main.numberOfNodes > 1 )
			{
				//System.out.println(name + " Is waiting... still = "+ TestClass.numberOfNodes);
				Main.numberOfNodes--;
				try {LockSyncPoint.wait();} 
				catch (InterruptedException e) {e.printStackTrace();}
			}
			else
			{
				//System.out.println(name + " I am the last");
				LockSyncPoint.notifyAll();
			}
		}
		
		System.out.println("*******************************************************************");
		

		ReadingThread reading = null ;
		WritingThread writing = null ;
		
		/**open the communication streams -- the read process was implemented in a separated thread to ensure a pseudo-parallel computing*/
		//for each one of my adjacent nodes i will open 2 communication channels
		for ( String myAdjNodeKey : terminationSockets.keySet())
		{
			Socket myAdjNodeSocket = terminationSockets.get(myAdjNodeKey) ;
			
			try {writing = new WritingThread(myAdjNodeSocket, routingTable) ;} 
			catch (IOException e1) {e1.printStackTrace();}
			catch (Exception e1) {e1.printStackTrace();}
			
			try { reading = new ReadingThread(myAdjNodeSocket, routingTable , linkCosts, gateways) ;} 
			catch (IOException e) {e.printStackTrace();}
			
			//receive from adjacent
			if ( reading != null )
			{
				reading.start();
			}
			else System.out.println("Reading variable is null");	
			
			//send to adjacent
			if ( writing != null )
			{
				writing.start();
			}
			else System.out.println("Writing variable is null");

		}
	}
}