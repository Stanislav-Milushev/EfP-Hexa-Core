package org.mbe.sat.assignment;

import metaFeatureModel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.util.SystemInfo;
import metaFeatureModel.FeatureModel;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import metaFeatureModel.Feature;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Parses XML files of FeatureIDE models into an instance of the corresponding meta model.
 */
public class XMLToEMFParser {

    private static final Group ALT_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group AND_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group OR_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private FeatureModel featureModel = null;
    private int currentNumber = 1;
    org.slf4j.Logger log = LoggerFactory.getLogger(XMLToEMFParser.class);
    
    public XMLToEMFParser() {
        // Example of how to instantiate elements of the Meta model
        // Initializing Groups and an empty FeatureModel from src-gen[main]
//        final Group altGroup;
        // MetaFeatureModelFactory.eINSTANCE.createGroup() creates Groups from FeatureModel
        ALT_GROUP_IDENTIFIER.setGroupType(GroupType.ALT);
        AND_GROUP_IDENTIFIER.setGroupType(GroupType.AND);
        OR_GROUP_IDENTIFIER.setGroupType(GroupType.OR);
    }

    /**
     * TODO: Parse the XML generated from the featureModel (without CrossTree) into featureModel
     * Note: Every Feature of the FeatureModel should get a number, starting by 1 e.g. feature.setNumber(1)
     */
    public FeatureModel parse(String uri) throws ParserConfigurationException, IOException, SAXException {

        featureModel = MetaFeatureModelFactory.eINSTANCE.createFeatureModel();

        // Parse XML document into  meta model
        // Initializing an Document for parsing the XML with Java Dom Parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.parse(uri);
        

        // TODO: Solution goes here
        // Use MetaFeatureModelFactory.eInstance... to create Features etc.
        NodeList rootNodeList = xmlDocument.getElementsByTagName("struct");
        
        
        // falls kein feature model vorhanden ist, leeres featuremodel zurückgeben
        if(rootNodeList.getLength() <= 0 || rootNodeList.getLength() > 1) {return featureModel;} 
        
        // item 0 ist die root node <struct>
    	Node rootNode = rootNodeList.item(0).getChildNodes().item(1);
    
    	// im struct gibt es eine root-gruppe, die wir erstellen
    	Group rootGroup = getRootGroup(rootNode);
    	//Root feature erstellen
        Feature root = createFeature(rootNode, rootGroup);
        log.debug("Root Group Type: " +rootGroup.getGroupType().toString());
        log.debug("Root Feature Name: " + root.getName());
    	
    	// am ende setzen wir nur noch die root ins featureModel,
    	// da in der root alle features durch gruppen enthalten sind
    	featureModel.setRoot(root);

        return featureModel;
    }
    
    /*
     * Nur für die Root Node gedacht.
     * Checken ob AND OR oder ALT Gruppe ist und dementsprechend erstellen
     * */
    private Group getRootGroup(Node root) {
    	Group group = MetaFeatureModelFactory.eINSTANCE.createGroup();
    	Node currentNode= root.getChildNodes().item(1); // Holt die erste Gruppe
		String nodeName = currentNode.getNodeName();
		switch(nodeName) {
			case "and": {
				group.setGroupType(AND_GROUP_IDENTIFIER.getGroupType());
				break;
				}
				
			case "or": {
				group.setGroupType(OR_GROUP_IDENTIFIER.getGroupType());
				break;
			}
				
			case "alt": {
				group.setGroupType(ALT_GROUP_IDENTIFIER.getGroupType());
				break;
			}
		}
		return group;
    }
    
    /*
     * Rekursive Funktion!
     * Für jedes Child-Feature:
     * Checken ob es eine weitere Gruppe ist oder ein einzelnes Feature (== Ende des Astes im Baum) 
     * Erstellt ein Feature, was wieder eine Gruppe haben/sein kann (und die funktion erneut aufruft)
     * und der aktuellen Gruppe hinzufügt
     */
    private Feature setChilds(Node parent, Group currentGroup, Feature parentFeature){
    	NodeList childList = parent.getChildNodes();
    	Feature feature = null;
    	for(int i = 0; i < childList.getLength(); i++) {
    		Node currentNode = childList.item(i);
    		if (currentNode.getNodeType() == Node.ELEMENT_NODE) { // Childelemente bekommen
    			String nodeName = currentNode.getNodeName();
    			switch(nodeName) {
    				case "and": {
    					Group group = MetaFeatureModelFactory.eINSTANCE.createGroup();
    					group.setGroupType(AND_GROUP_IDENTIFIER.getGroupType());
    					feature = createFeature(currentNode, group); // rekursiv
    					// das erstellte feature wird im parent als child hinzugefügt
    					parentFeature.getChildren().add(feature); 
    					break;
    					}
    					
    				case "or": {
    					Group group = MetaFeatureModelFactory.eINSTANCE.createGroup();
    					group.setGroupType(OR_GROUP_IDENTIFIER.getGroupType());
    					feature = createFeature(currentNode, group);// rekursiv
    					parentFeature.getChildren().add(feature);
    					break;
    				}
    					
    				case "alt": {
    					Group group = MetaFeatureModelFactory.eINSTANCE.createGroup();
    					group.setGroupType(ALT_GROUP_IDENTIFIER.getGroupType());
    					feature = createFeature(currentNode, group);// rekursiv
    					parentFeature.getChildren().add(feature);
    					break;
    				}
    					
    				case "feature": {
    					feature = createFeature(currentNode);// NICHT rekursiv, Ende eines Zweigs
    					parentFeature.getChildren().add(feature);
    					
    					break;
    				}	
    			}
    		}
    	}
    	return feature;
    }
    
    
    /*
     * Erstellt ein Feature ohne setChilds ein weiteres mal aufzurufen (Ende der Rekursion)
     */
    private Feature createFeature(Node node) {
		String name = getNodeAttribute(node, "name");
		String abst = getNodeAttribute(node, "abstract");
		String mandatory = getNodeAttribute(node, "mandatory");
    	
		Feature feature = MetaFeatureModelFactory.eINSTANCE.createFeature();
		feature.setName(name);
		feature.setAbstract(abst == "true" ? true : false); // string zu bool
		feature.setMandatory(mandatory == "true" ? true : false); // string zu bool
		feature.setNumber(currentNumber++); // ID des Features, danach wird die global currentNumber erhöht
		log.debug("Create Feature WITHOUT Group: Feature-Name - " + name);
		return feature;
    }
    
    /*
     * Erstellt ein Feature und bekommt eine Gruppe.
     * Ruft erneut setChilds() auf mit der Gruppe. 
     */
    private Feature createFeature(Node node, Group group) {
		String name = getNodeAttribute(node, "name");
		String abst = getNodeAttribute(node, "abstract");
		String mandatory = getNodeAttribute(node, "mandatory");
		log.debug("Create Feature with Group: Type - "+group.getGroupType().toString()+", Feature-Name - " + name);
		Feature feature = MetaFeatureModelFactory.eINSTANCE.createFeature();
		feature.setName(name);
		feature.setAbstract(abst == "true" ? true : false);
		feature.setMandatory(mandatory == "true" ? true : false);
		feature.setNumber(currentNumber++); // ID des Features, danach wird die global currentNumber erhöht
		feature.setGroup(group);
		setChilds(node, group, feature);
		return feature;
    }
    
    /*
     * typesafe variante ein node attribute zu bekommen 
     * ohne gefahr zu laufen dass eine exception schießt
     */
    private String getNodeAttribute(Node node, String key) {
    	if(!node.hasAttributes()) {
    		return "";
    	}
    	
    	Node item = node.getAttributes().getNamedItem(key);
    	if(item == null) {
    		return "";
    	}
    	
    	return item.getNodeValue();
    }
    
    

}