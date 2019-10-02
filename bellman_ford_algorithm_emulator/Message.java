package bellman_ford_algorithm_emulator;

/**
 * @author : Hicham Elhaloumi
 * @description : Message, represents the format of the message that should be sent over the network
 * @date : (last modification) 29/09/2019
 */
import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//main attributes
	private String source ; // name of the node -- normally we should make the address here, but as long as we are working in the localhost we use the name of the node
	private HashMap<String, Integer> vectorTable = new HashMap<>() ;
	
	public Message(String source , HashMap<String, Integer> vectorTable)
	{
		this.source = source ;
		this.vectorTable = vectorTable ;
	}
	
	//getters and setters
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public HashMap<String, Integer> getVectorTable() {
		return vectorTable;
	}
	public void setVectorTable(HashMap<String, Integer> vectorTable) {
		this.vectorTable = vectorTable;
	}
	
}
