package bellman_ford_algorithm_emulator;

/**
 * @author : HICHAM ELHALOUMI
 * @description : Main class, in which we specify the parameters of the network to be emulated.
 * @date : (last modification) 29/09/2019
 * @version : V0.2
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
	
	//initial port
	static Integer globalPort = 1024 ;
	
	//default number of nodes
	static Integer numberOfNodes = 6 ;
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("\t**********************************");
		System.out.println("\t*************Welcome*************");
		System.out.println("\t**********************************\n");

		System.out.println("(*)Please select a mode :\n\n\t1-Default configuration?\n\t2- Customized Configuration?");
		System.out.print("\n\n Your choice please : ");
		int choice = new Scanner(System.in).nextInt() ;
		if ( choice == 1)
			runDefaultConfiguration();
		else if (choice == 2)
			runCustomizedEmulation();
		else 
			System.out.println("\n Invalide choice.. the program will be closed");
					
	}
	
	//port generation method
	public static Integer getNewPort()
	{
		return globalPort++;
	}
	
	/*****************************************************************************************
	 * this configuration contains 6 nodes A,B,C,D,E and F described in the attached document
	 ******************************************************************************************/
	//run a default configuration
	public static void runDefaultConfiguration()
	{
		System.out.println("\t*********You have selected default mode***********\n");
		///setting informations 
				//nodes definition
				Node t1 = new Node("A") ;
				Node t2 = new Node("B") ;
				Node t3 = new Node("C") ;
				Node t4 = new Node("D") ;
				Node t5 = new Node("E") ;
				Node t6 = new Node("F") ;
				
				/**set neibors informations*/
				
				//node A with B ie. t1 with t2
				Integer nPort = getNewPort() ;//each communication has a  port number
				
				t1.getAdjacentNodes().put(t2.getAdress()+"|"+nPort, t2) ; //add t2 as adjacent to t1 table
				t1.getIsServerWithAdjacent().put(String.valueOf(nPort), true);//set t1 as a server through the nPort
				t1.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);//in the beginning there is no connection //TODO : when establish make it true
				t1.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t2);
				t1.getLinkCosts().put(t2.getNName(), 2) ;//add the name of the other terminal
				
				t2.getAdjacentNodes().put(t1.getAdress()+"|"+nPort, t1) ;
				t2.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t2.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);//in the beginning there is no connection
				t2.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t1);
				t2.getLinkCosts().put(t1.getNName(), 2) ;

				// t1 with t3
				nPort = getNewPort() ;//get a new port

				t1.getAdjacentNodes().put(t3.getAdress()+"|"+nPort, t3) ; //add t3 as adjacent to t1 table
				t1.getIsServerWithAdjacent().put(String.valueOf(nPort), false);//set t1 as a server through the nPort
				t1.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);//in the beginning there is no connection
				t1.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t3);
				t1.getLinkCosts().put(t3.getNName(), 2) ;

				
				t3.getAdjacentNodes().put(t1.getAdress()+"|"+nPort, t1) ;
				t3.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t3.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);//in the beginning there is no connection
				t3.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t1);
				t3.getLinkCosts().put(t1.getNName(), 2) ;

				// t1 with t4
				nPort = getNewPort() ;//get a new port

				t1.getAdjacentNodes().put(t4.getAdress()+"|"+nPort, t4) ;
				t1.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t1.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t1.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t4);
				t1.getLinkCosts().put(t4.getNName(), 2) ;

				t4.getAdjacentNodes().put(t1.getAdress()+"|"+nPort, t1) ;
				t4.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t4.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t4.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t1);
				t4.getLinkCosts().put(t1.getNName(), 2) ;

				// t3 with t5
				nPort = getNewPort() ;//get a new port

				t3.getAdjacentNodes().put(t5.getAdress()+"|"+nPort, t5) ;
				t3.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t3.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t3.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t5);
				t3.getLinkCosts().put(t5.getNName(), 2) ;

				t5.getAdjacentNodes().put(t3.getAdress()+"|"+nPort, t3) ;
				t5.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t5.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t5.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t3);
				t5.getLinkCosts().put(t3.getNName(), 2) ;
				
				//t2 with t5
				nPort = getNewPort() ;//get a new port

				t2.getAdjacentNodes().put(t5.getAdress()+"|"+nPort, t5) ;
				t2.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t2.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t2.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t5);
				t2.getLinkCosts().put(t5.getNName(), 5) ;

				t5.getAdjacentNodes().put(t2.getAdress()+"|"+nPort, t2) ;
				t5.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t5.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t5.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t2);
				t5.getLinkCosts().put(t2.getNName(), 5) ;
				
				//t4 with t5
				nPort = getNewPort() ;//get a new port

				t4.getAdjacentNodes().put(t5.getAdress()+"|"+nPort, t5) ;
				t4.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t4.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t4.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t5);
				t4.getLinkCosts().put(t5.getNName(), 1) ;

				t5.getAdjacentNodes().put(t4.getAdress()+"|"+nPort, t4) ;
				t5.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t5.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t5.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t4);
				t5.getLinkCosts().put(t4.getNName(), 1) ;
				
				//t4 with t6
				nPort = getNewPort() ;//get a new port

				t4.getAdjacentNodes().put(t6.getAdress()+"|"+nPort, t6) ;
				t4.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t4.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t4.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t6);
				t4.getLinkCosts().put(t6.getNName(), 1) ;

				t6.getAdjacentNodes().put(t4.getAdress()+"|"+nPort, t4) ;
				t6.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t6.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t6.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t4);
				t6.getLinkCosts().put(t4.getNName(), 1) ;
				
				//t5 with t6
				nPort = getNewPort() ;//get a new port

				t5.getAdjacentNodes().put(t6.getAdress()+"|"+nPort, t6) ;
				t5.getIsServerWithAdjacent().put(String.valueOf(nPort), false);
				t5.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t5.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t6);
				t5.getLinkCosts().put(t6.getNName(), 1) ;

				t6.getAdjacentNodes().put(t5.getAdress()+"|"+nPort, t5) ;
				t6.getIsServerWithAdjacent().put(String.valueOf(nPort), true);
				t6.getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false);
				t6.getPortesAssociatedToAdjacents().put(String.valueOf(nPort), t5);
				t6.getLinkCosts().put(t5.getNName(), 1) ;
				
				

				t1.describeNode();
				t2.describeNode();
				t3.describeNode();
				t4.describeNode();
				t5.describeNode();
				t6.describeNode();
				
				//run
				t1.start();
				t2.start();
				t3.start();
				t4.start();
				t5.start();
				t6.start();
	}
	
	public static void runCustomizedEmulation()
	{
		
		ArrayList<Node> listNodes = new ArrayList<>() ; 
		
		System.out.println("\n\t*********You have selected customized mode***********");
		
		//select the node's number
		do {
			System.out.print("\n(*) Please select the number of nodes : ");
			try
			{
				numberOfNodes =  new Scanner(System.in).nextInt() ;
			}catch (NumberFormatException e) {}
			
		}while (numberOfNodes<0 || numberOfNodes>100);//we assume that the size of our graph will not goes more than 100!!
		
		
		System.out.println("\n\tYou have defined "+ numberOfNodes +" nodes in your network");
	
		int counter = 0 ; 
		while(counter<numberOfNodes)
		{
			
			String name ="";
			do {
				System.out.print("\n\tPlease define a 'none empty' label of the node number "+ (++counter) + ": ");
				name= new Scanner(System.in).nextLine() ;
			}while(name.trim().equals("")) ;//while the string is empty -- ask the the user again
			
			listNodes.add(new Node(name)) ;
		}
		
		System.out.print("\n\n******The nodes of your system are :"+ listNodes);
		
		System.out.println("\n\n(*) Now you have to select for each node its adjacent nodes and the associated cost");

		counter = 0 ;
		while (counter < numberOfNodes)
		{
			String adjacentNames = "";
			
			do
			{
				System.out.print("\n\tPlease choose 'ALL' the adjacents nodes of " + listNodes.get(counter).getNName() + " (EXAMPLE : B C D) : ");
				adjacentNames = new Scanner(System.in).nextLine();
			}while(adjacentNames.trim().equals("")) ;
			
			System.out.print("\n\t You choosed : "+ adjacentNames+"\n");
			
			//get the associated references
			ArrayList<Node> adjacents = getAdjacents(adjacentNames, listNodes) ;
			
			//link the actual node with its adjacent nodes
			Integer counterAdj = 0 ;
			while (counterAdj < adjacents.size())
			{
				//if the adjacent is not already defined then we'll define it -- adjacents.get(counterAdj) already in listNodes.get(counter).getAdjacentNodes()
				if ( !listNodes.get(counter).getAdjacentNodes().containsValue(adjacents.get(counterAdj)))
				{
					Integer nPort = getNewPort() ; //each communication has a  port number
					listNodes.get(counter).getPortesAssociatedToAdjacents().put(String.valueOf(nPort), adjacents.get(counterAdj)) ;
					listNodes.get(counter).getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false) ;
					listNodes.get(counter).getAdjacentNodes().put(adjacents.get(counterAdj).getAdress()+"|"+nPort, adjacents.get(counterAdj)) ;
					listNodes.get(counter).getIsServerWithAdjacent().put(String.valueOf(nPort), false);
					
					System.out.print("\n\t => please select the cost associated to the link with "+ adjacents.get(counterAdj).getNName() + ":");
					
					Integer cost = -1;
					do {
						try{
							 cost = new Scanner(System.in).nextInt() ;
						}catch( NumberFormatException e) {}
					}while(cost < 0);//to avoid storming circuits we use only positive weights
					
					listNodes.get(counter).getLinkCosts().put(adjacents.get(counterAdj).getNName(), cost) ;

					//do the same thing for the other end
					adjacents.get(counterAdj).getPortesAssociatedToAdjacents().put(String.valueOf(nPort), listNodes.get(counter)) ;
					adjacents.get(counterAdj).getStatusIsConnectedWithAdjacent().put(String.valueOf(nPort), false) ;
					adjacents.get(counterAdj).getAdjacentNodes().put(listNodes.get(counter).getAdress()+"|"+nPort, listNodes.get(counter)) ;
					adjacents.get(counterAdj).getIsServerWithAdjacent().put(String.valueOf(nPort), true);
					
					adjacents.get(counterAdj).getLinkCosts().put(listNodes.get(counter).getNName(), cost) ;
				}else
				{
					System.out.println("\n /!\\ "+adjacents.get(counterAdj).getNName() + " Is already linked with "+ listNodes.get(counter).getNName() +"; Its the cost = "+listNodes.get(counter).getLinkCosts().get(adjacents.get(counterAdj).getNName()));
				}
				
				counterAdj++ ;
			}
			
			counter++;
		}
		
		System.out.println("\n\n(*)Nodes Description:\n");
		//describe nodes
		counter = 0 ;
		while (counter <numberOfNodes)
		{
			listNodes.get(counter).describeNode();
			counter++;
		}
		
		System.out.println("\n\n\t************************************************\n\tPreparation,<PRESS ENTER TO RUN THE SIMULATION>\n\t**************************************************\n");
		new Scanner(System.in).nextLine();
		
		counter = 0 ;
		while (counter <numberOfNodes)
		{
			listNodes.get(counter).start();
			counter++;
		}
		
		
	}
	
	//get the references objects list associated to the list of names 'adjacentNames'
	public static ArrayList<Node> getAdjacents(String adjacentNames, ArrayList<Node> listOfNodes)
	{
		ArrayList<Node> adj = new ArrayList<>() ;
		
		StringTokenizer st = new StringTokenizer(adjacentNames," ");
		String nodeName = "" ;
	    while (st.hasMoreTokens())
         {
	    	nodeName = st.nextToken() ;
	    	adj.add(getNodeRefByName(listOfNodes, nodeName)) ;
         }
	     
		
		return adj;
	}
	//get the reference object associated to a given name 
	public static Node getNodeRefByName(ArrayList<Node> nodes , String name)
	{
		Node associatedNode = null ;
		
		for ( Node node : nodes )
		{
			if ( node.getNName().equals(name ))
			{
				associatedNode = node ;
			}
		}
		
		return associatedNode ;
		
	}
	
}