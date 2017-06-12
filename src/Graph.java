import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph implements Cloneable{
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
		
	}
	
    private Map<String, LinkedHashSet<String>> map = new LinkedHashMap();
    
    public void addEdge(String node1, String node2) {  	
    	//int index=node1.indexOf("@");
    	//node1=node1.substring(0,index);
    	LinkedHashSet<String> adjacent = map.get(node1);
        if(adjacent==null) {
            adjacent = new LinkedHashSet();
            map.put(node1, adjacent);
        }
        adjacent.add(node2);
        
        //System.out.println(map);
    }
    
    public void connectGraphs(){
    	 for(String s1: map.keySet()){  
    		  // System.out.println(m.getKey()+" "+m.getValue());
    		 String speciesName_1=s1.toString();
    		 int index_1=speciesName_1.indexOf("@");
    		 String speciesName_withoutFileId_1=speciesName_1.substring(0,index_1);
    		 //System.out.println(speciesName_withoutFileId_1);
    		 
    		 for(String s2: map.keySet()){
    			 String speciesName_2=s2.toString();
    			 int index_2=speciesName_2.indexOf("@");
    			 String speciesName_withoutFileId_2=speciesName_2.substring(0,index_2);
    			 
    			 if( !(speciesName_1.equalsIgnoreCase(speciesName_2)) && speciesName_withoutFileId_1.equalsIgnoreCase(speciesName_withoutFileId_2))  {
    				 
    				 LinkedHashSet<String> node1 = map.get(speciesName_1);
    				 node1.add(speciesName_2);
    				 map.put(speciesName_1, node1);
    				 
    				 LinkedHashSet<String> node2 = map.get(speciesName_2);
    				 node2.add(speciesName_1);
    				 map.put(speciesName_2, node2);
    				 
    			 }
    		 }
    	}
    }
    public String adjacentNodesFirstTime(String last) {
    	
    		for(String s1: map.keySet()){  
    			 String speciesName_1=s1.toString();
        		 int index_1=speciesName_1.indexOf("@");
        		 String speciesName_withoutFileId_1=speciesName_1.substring(0,index_1);
        	
        		 if(speciesName_withoutFileId_1.equalsIgnoreCase(last)){
        			 LinkedHashSet<String> adjacent = map.get(speciesName_1);
        		        //System.out.println(adjacent);
        		        if(adjacent==null) {      		        
        		            return null;
        		        }
        		        return speciesName_1;
        		 }
    		//int index=last.indexOf("@");
        	//last=last.substring(0,index);
    		}
			return null;
    }
    public List<String> getStartNodes(String last) {
    	List<String> startNodeList = new ArrayList<String>();
		
    	for(String s1: map.keySet()){  
			 String speciesName_1=s1.toString();
    		 int index_1=speciesName_1.indexOf("@");
    		 String speciesName_withoutFileId_1=speciesName_1.substring(0,index_1);
    	
    		 if(speciesName_withoutFileId_1.equalsIgnoreCase(last)){
    		  	startNodeList.add(speciesName_1);	        
    		 }
		}
		return startNodeList;
}
    public LinkedList<String> adjacentNodes(String last) {
    
    
    		//System.out.println(last);
       	 	LinkedHashSet<String> adjacent = map.get(last);
           //System.out.println(adjacent);
           if(adjacent==null) {
               return new LinkedList();
           }
           return new LinkedList<String>(adjacent);   	
    }
    
    
   public Map<String, LinkedHashSet<String>> getMap(){
    	return map;
    }
   
   public int getNodeCount(){
	   int count=0;
	   for(String s1: map.keySet()){  
		   count+=1;
	   }
	   return count;
   }
	public void checkAdjecent(String START) {
		
		LinkedHashSet<String> adjacent = map.get(START);
		int index_1 = START.indexOf("@");
		String START_withoutFileId_1 = START.substring(0,index_1);
		
		for(String node : adjacent) {
			//System.out.println(node);
			int index_2 = node.indexOf("@");
			String node_withoutFileId_1 = node.substring(0,index_2);
			if(START_withoutFileId_1.equalsIgnoreCase(node_withoutFileId_1)) {
				adjacent.remove(node);
				map.put(START, adjacent);
			}
		}
	}
   /*public void removeEdge(String startNode) {
	  LinkedHashSet<String> adjecent = map.get(startNode);
	
	  for(String node : adjecent) {
		 int index_1=speciesName_1.indexOf("@");
 		 String speciesName_withoutFileId_1=speciesName_1.substring(0,index_1);
		  if() {
			  
		  }
	  }
	}*/

}