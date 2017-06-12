import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class SearchParallel {
	//private static  String START="JAK2";
    //private static String END = "STAT5-P";
	
	private static String START;
	private static String END;
    
	List<List<String>> results;
    
	public static void main(String[] args) {
	
		long startTime = System.currentTimeMillis();
		Graph graph=new Graph();
		XMLReader xmlReader=new XMLReader();
		PropertiClass ps = new PropertiClass();
		
		try {
			START = ps.getStart();
			END = ps.getEnd();
			
			//location of SBML files
			String filePath=ps.getFilePath();
			File[] all_files=new File(filePath).listFiles();
			
			//Reading all edges file by file
			for(File f: all_files){
				xmlReader.addEdges(f.getName(),filePath);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		//Create Graph
		String s1 = null;
		String s2 = null;
		String s3 = null;
		
		for(LinkedList<String> edge: xmlReader.getEdges()){
			//System.out.println(node);
			for(String e : edge){
				if(s1==null){
					s1=e;
				}
				else{
					s2=e;
					s3=s1;
					s1=null;
				}
			}
			//System.out.println(s3+" "+s2);
			if(s3.equalsIgnoreCase(s2)) {
				continue;
				//System.out.println("same");
			} else{
				graph.addEdge(s3, s2);
				//continue;
			}
			//graph.addEdge(s3, s2);			
		}
		//Connect disjoint Graphs into one graph
		graph.connectGraphs();
		
		//searchParallel1(graph, startTime);
		searchParallel2(graph, startTime);
		
		
		//new SearchParallel().printResults(future);


	}
	public static void searchParallel1(Graph graph, long startTime) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		//Future< List<List<String>>> future;
		List <Future<List<List<String>>>> futureList = new LinkedList<Future<List<List<String>>>>();
		
		
		for(String startNode : graph.getStartNodes(START)) {
			//LinkedList<String> visited = new LinkedList<String>();
			//visited.add(startNode);
			
			/*if(!nodeCount.isEmpty()) {
				nodeCount.clear();
			}*/						
			/*LinkedList<String> firstAdjecents = graph.adjacentNodes(startNode);
			int index_1 = startNode.indexOf("@");
			String startNode_withoutFileId_1 = startNode.substring(0,index_1);
			
			for(String node : firstAdjecents) {
				int index_2 = node.indexOf("@");
				String node_withoutFileId_1 = node.substring(0,index_2);
				
				if(startNode_withoutFileId_1 .equalsIgnoreCase(node_withoutFileId_1)) {
					graph.(startNode);
				}
			}*/
			Future<List<List<String>>> future = executor.submit(new ParallelDFS(graph,startNode,END));
			futureList.add(future);		
			//srh.depthFirst(graph, visited);
			//executor.shutdown();
			
			//System.out.println("All tasks are submited");
			
		}		
		
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("Available Processors "+processors);
		int size = 0;
		for(Future<List<List<String>>> future : futureList) {
			try {				
				//System.out.println("Size " + future.get().size());
				size+=future.get().size();
				
				/*for(List<String> it: future.get()) {
					for(String s: it) {
						int index2=s.indexOf("@");
			        	String end=s.substring(0,index2);
						if(end.equalsIgnoreCase(END)){
							System.out.print(s + "");
						}
						else{
							System.out.print(s + "->");
						}				
					}
					System.out.println("");
				}*/
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			} catch (ExecutionException e) {
	
				e.printStackTrace();
			}
		}		
		executor.shutdown();
		long endTime = System.currentTimeMillis();
		
		System.out.println("Execution time: " + (endTime - startTime) / 1000.0 + " seconds");
		System.out.println("Number Of Paths: "+size);
		

	}
	
	public static void searchParallel2(Graph graph, long startTime) {
		
		int index_1, index_2;
		String startNode_withoutFileId, adjecent_withoutFileId;
		
		LinkedList<String> visited;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		//Future< List<List<String>>> future;
		List <Future<List<List<String>>>> futureList = new LinkedList<Future<List<List<String>>>>();
		
		try {
			
			graph = (Graph) graph.clone();
			
			for(String startNode : graph.getStartNodes(START)) {
				//System.out.println(startNode);
				//System.out.println(	graph.adjacentNodes(startNode));
				
				visited = new LinkedList<String>();
				visited.add(startNode);
				for(String adjecent :graph.adjacentNodes(startNode)) {
					
					index_1 = startNode.indexOf("@");
					startNode_withoutFileId = startNode.substring(0,index_1);
					
					index_2 = adjecent.indexOf("@");
					adjecent_withoutFileId = adjecent.substring(0,index_2);
					
					if(startNode_withoutFileId.equalsIgnoreCase(adjecent_withoutFileId )) {
						continue;
					}
					visited.add(adjecent);
					Future<List<List<String>>> future = executor.submit(new ParallelDFS_1(graph,END,visited));
					futureList.add(future);	
					visited.removeLast();
		
				}
			
			}
			int size = 0;
			for(Future<List<List<String>>> future : futureList) {
				try {				
					//System.out.println("Size " + future.get().size());
					size+=future.get().size();
					
					/*for(List<String> it: future.get()) {
						for(String s: it) {
							int index2=s.indexOf("@");
				        	String end=s.substring(0,index2);
							if(end.equalsIgnoreCase(END)){
								System.out.print(s + "");
							}
							else{
								System.out.print(s + "->");
							}				
						}
						System.out.println("");
					}*/
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				} catch (ExecutionException e) {
		
					e.printStackTrace();
				}
			}		
			executor.shutdown();
			long endTime = System.currentTimeMillis();
			
			System.out.println("Execution time: " + (endTime - startTime) / 1000.0 + " seconds");
			System.out.println("Number Of Paths: "+size);
			
		} catch (CloneNotSupportedException e) {
			
			e.printStackTrace();
			
		}
	
	}
	/*public void printResults(Future< List<List<String>>> future){
		List<List<String>> results;
		try {
			results = future.get();
			
			for(List<String> it: results) {
				for(String s: it) {
					int index2=s.indexOf("@");
		        	String end=s.substring(0,index2);
					if(end.equalsIgnoreCase(END)){
						System.out.print(s + "");
					}
					else{
						System.out.print(s + "->");
					}				
				}
				System.out.println("");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	

}
