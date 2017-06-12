import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

	private List<LinkedList<String>> edges = new ArrayList<LinkedList<String>>();
	
	//store species id & name
	private Map<String, String> species_list = new LinkedHashMap();
	
	public void addEdges(String file_name,String filePath){
		try {
				//System.out.println(file_name);				
				List<String> reactants=new LinkedList<String>();
				List<String> modifiers=new LinkedList<String>();
				List<String> edge=new LinkedList<String>();
				String products=null;
				boolean reversible=true;
				String species_id,species_name;
				
				FileInputStream file = new FileInputStream(new File(filePath+file_name));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        dBuilder = dbFactory.newDocumentBuilder();
	
		        Document doc = dBuilder.parse(file);
		        doc.getDocumentElement().normalize();
		        XPath xPath =  XPathFactory.newInstance().newXPath();
		         
		        //Species
		        String species_expression = "//listOfSpecies/species";	
		        NodeList listOfSpecies = (NodeList) xPath.compile(species_expression).evaluate(doc, XPathConstants.NODESET);
		         
		        for (int i = 0; i < listOfSpecies .getLength(); i++) {
		        	Node species = listOfSpecies .item(i);
		            //System.out.println( nNode.getNodeName());
		        	if (species.getNodeType() == Node.ELEMENT_NODE) {
		        		Element speciesElement = (Element) species;
		        		
		        		species_id=speciesElement.getAttribute("id");
		        		species_name=speciesElement.getAttribute("name");
		        		//System.out.println(species_id+" "+species_name);
		        		if(speciesElement.getAttribute("name").isEmpty()){
		        			species_list.put(species_id,species_id);
		        		}
		        		else{
		        			species_list.put(species_id,species_name);			
		        		}		                                
		            }  
		         }
		         //System.out.println("\n");
		        //System.out.println(species_list);		
		        
		        //Species Aliases
		        String expression_id = "//listOfSpeciesAliases/speciesAlias";	
		        NodeList nodeList_id = (NodeList) xPath.compile(expression_id).evaluate(doc, XPathConstants.NODESET);
		        for (int i = 0; i < nodeList_id.getLength(); i++) {
		        	Node nNode = nodeList_id.item(i);
		            //System.out.println( nNode.getNodeName());
		        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		Element eElement = (Element) nNode;
		                //System.out.println( 
		                eElement.getAttribute("id");
		            }  		         
		         }
		        // System.out.println("\n");		
		        
		        //Reaction
		        String expression_reaction = "//listOfReactions/reaction";	
		        NodeList list_of_reactions = (NodeList) xPath.compile(expression_reaction).evaluate(doc, XPathConstants.NODESET);
		         
		        for (int i = 0; i < list_of_reactions.getLength(); i++) {
		        	Node reaction = list_of_reactions.item(i); 
					if (reaction.getNodeType() == Node.ELEMENT_NODE) {
						
						if(!reactants.isEmpty()){
							reactants.clear();
						}
						if(!modifiers.isEmpty()){
							modifiers.clear();
						}
						Element reactionElement = (Element) reaction;
						reversible=reactionElement.getAttribute("reversible").isEmpty();
						//System.out.println(reactionElement.getAttribute("reversible").isEmpty());	
				
						int list_of_Reactants_length = reactionElement.getElementsByTagName("listOfReactants").getLength();
						int listOfProducts_length =reactionElement.getElementsByTagName("listOfProducts").getLength();
						int listOfModifiers_length =reactionElement.getElementsByTagName("listOfModifiers").getLength();
						
						//System.out.println(list_of_Reactants_length);
						if(list_of_Reactants_length>0 && listOfProducts_length>0){
							Element list_of_Reactants = (Element) reactionElement.getElementsByTagName("listOfReactants").item(0);
							Element listOfProducts =(Element) reactionElement.getElementsByTagName("listOfProducts").item(0);
							Element listOfModifiers =(Element) reactionElement.getElementsByTagName("listOfModifiers").item(0);
						
							NodeList list_of_species_reference =list_of_Reactants.getElementsByTagName("speciesReference");	 
							
							//Reactant
							for(int j=0;j<list_of_species_reference.getLength();j++){
								Element reactants_species_reference =(Element) list_of_species_reference.item(j);
								reactants.add(species_list.get(reactants_species_reference.getAttribute("species"))+"@"+file_name);
								//reactants.add(species_list.get(reactants_species_reference.getAttribute("species")));
								//System.out.println(species_list.get(reactants_species_reference.getAttribute("species"))+"@"+file_name);
								//reactants.add(reactants_species_reference.getAttribute("species"));
								
								//System.out.println("reactansts "+species_list.get(reactants_species_reference.getAttribute("species")));
							}														
							//products
							for(int j=0;j<listOfProducts.getElementsByTagName("speciesReference").getLength();j++){
									
								Element product_speciesReference =(Element) listOfProducts.getElementsByTagName("speciesReference").item(j);
								//products=species_list.get(product_speciesReference.getAttribute("species")); 
								products=species_list.get(product_speciesReference.getAttribute("species")).concat("@"+file_name); 	 
								//System.out.println("product "+species_list.get(product_speciesReference.getAttribute("species")));
							}	
							
							//listOfModifires
							if(listOfModifiers_length>0){
								for(int j=0;j<listOfModifiers.getElementsByTagName("modifierSpeciesReference").getLength();j++){									
									Element Modifiers_speciesReference =(Element) listOfModifiers.getElementsByTagName("modifierSpeciesReference").item(j);
									modifiers.add(species_list.get(Modifiers_speciesReference .getAttribute("species"))+"@"+file_name);
									//reactants.addAll(modifiers);
									//products=species_list.get(product_speciesReference.getAttribute("species")); 
									//products=species_list.get(product_speciesReference.getAttribute("species")).concat("@"+file_name); 	 
									//System.out.println(Modifiers_speciesReference .getAttribute("species"));
								}	
								reactants.addAll(modifiers);
							}						
							//System.out.println();
							// System.out.println(node);						 
							for(String reactant : reactants){
								if(!edge.isEmpty()){
									edge.clear();
								}
								edge.add(reactant);
								edge.add(products);
								//System.out.println(edge);
							 
								edges.add(new LinkedList<String>(edge));
								if(reversible){
									if(!edge.isEmpty()){
										edge.clear();
									}
									edge.add(products);
									edge.add(reactant);
									edges.add(new LinkedList<String>(edge));
									//System.out.println(edges);
									//System.out.println("**");
								}
							}	
						}
							   
					}//reaaction get node type if condition              
		        }//reaction loop 		        
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {			
				e.printStackTrace();
			}		
	}
	
	public List<LinkedList<String>> getEdges(){
		//System.out.println(edges.size());	
		return edges;
	}
}
