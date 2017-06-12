import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class ParallelDFS_1 implements Callable< List<List<String>>> {

	private Graph graph;
	private List<List<String>> result = new LinkedList<List<String>>();
	private Map<String, Integer> nodeCount = new HashMap();
	private String END;
	
	LinkedList<String> visited = new LinkedList<String>();
	PropertiClass ps = new PropertiClass();
	int nodeCountvalue = ps.getNodeCountValue();
	
	public ParallelDFS_1(Graph graph, String END, LinkedList<String> visited) {
		
		this.graph = graph;
		this.END = END;
		this.visited.addAll(visited);
	}
	
	public  List<List<String>>  call() throws Exception {
				
		//graph.checkAdjecent(START);
		//System.out.println(graph.adjacentNodes(START));
		//visited.add(START);
		depthFirst(graph, visited);
		return result;
	}
	
	public void depthFirst(Graph graph, LinkedList<String> visited) {
		 
    	/*if(visited.isEmpty()){
    		visited.add(START);
    		String firstNode = graph.adjacentNodesFirstTime(visited.getLast());
    		visited.remove();
    		visited.add(firstNode);
    	}*/
		LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
    
        // examine adjacent nodes
        for (String node : nodes) {
           int index1=node.indexOf("@");
           String end1=node.substring(0,index1);
            if (visited.contains(node)) {
                continue;
            }         
            if (end1.equalsIgnoreCase(END)) {
            	visited.addLast(node);
                result.add(new LinkedList<String>(visited));
                //printPath(visited);           
                //visited.remove(hops);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
        	
        	int index2=node.indexOf("@");
        	String end2=node.substring(0,index2);
        	int count;
        	
         	if(nodeCount.get(node)==null){
         		nodeCount.put(node, 1);
         		//System.out.println(nodeCount);
         	}
         	else{
         		
         		count=nodeCount.get(node);
         		count+=1;
         		nodeCount.put(node, count);
         	}
         	
            if (visited.contains(node) || end2.equalsIgnoreCase(END) || nodeCount.get(node)>nodeCountvalue) {
            	continue;
            }
            visited.addLast(node);
            depthFirst(graph, visited);
            visited.removeLast();  
        }
        
    }

}
