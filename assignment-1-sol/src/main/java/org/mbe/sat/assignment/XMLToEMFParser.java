package org.mbe.sat.assignment;

import metaFeatureModel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XMLToEMFParser {

    private static final Group ALT_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group AND_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group OR_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private int featureNumber;

    public XMLToEMFParser() {

        ALT_GROUP_IDENTIFIER.setGroupType(GroupType.ALT);
        AND_GROUP_IDENTIFIER.setGroupType(GroupType.AND);
        OR_GROUP_IDENTIFIER.setGroupType(GroupType.OR);

    }

    public FeatureModel parse (String uri) throws IOException, SAXException, ParserConfigurationException{

        FeatureModel featureModel = MetaFeatureModelFactory.eINSTANCE.createFeatureModel();

        // Parse XML document into  meta model
        // Initializing an Document for parsing the XML with Java Dom Parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.parse(uri);

        //TODO: Solution
        featureNumber = 1;
        Node rootNode = createRoot(xmlDocument, featureModel);
        createFeatureTree(rootNode, featureModel.getRoot());


        return featureModel;
    }


    private Node createRoot(Document doc, FeatureModel featureModel){
        NodeList struct = doc.getElementsByTagName("struct");
        Node rootNode;
        if (!(struct.item(0).getFirstChild().getNodeType() == Node.ELEMENT_NODE))
            rootNode = struct.item(0).getFirstChild().getNextSibling();
        else
            rootNode = struct.item(0).getFirstChild();

        //Create a root Feature
        Feature root = MetaFeatureModelFactory.eINSTANCE.createFeature();
        Element eNodeRoot = (Element) rootNode;

        assignFeatureAttributes(root, eNodeRoot);
        featureModel.setRoot(root);
        return rootNode;
    }


    //Recursive call to create a nodeTree
    private void createFeatureTree(Node parent_node, Feature parent_feature) {
        int nodeLength = parent_node.getChildNodes().getLength();

        for (int i = 0; i < nodeLength; i++) {
            Node currentNode = parent_node.getChildNodes().item(i);
            if (currentNode.getNodeType() == Element.ELEMENT_NODE && !currentNode.getNodeName().equals("graphics")) {
                Element eCurrentNode = (Element) currentNode;
                Feature currentFeature = MetaFeatureModelFactory.eINSTANCE.createFeature();
                assignFeatureAttributes(currentFeature, eCurrentNode);
                parent_feature.getChildren().add(currentFeature);
                createFeatureTree(currentNode, currentFeature);
            }
        }
    }

    //Wrapper for getGroupType and setFeatureVariables
    private void assignFeatureAttributes(Feature feature, Element eNode){
        feature.setGroup(getGroupType(eNode));
        setFeatureVariables(feature, eNode);
    }

    private Group getGroupType(Element feature){
        String groupType = feature.getNodeName();

        switch (groupType) {
            case "and":
                return AND_GROUP_IDENTIFIER;
            case "or":
                return OR_GROUP_IDENTIFIER;
            case "alt":
                return ALT_GROUP_IDENTIFIER;
            default:
                return null;
        }
    }

    private void setFeatureVariables(Feature feature, Element eNode){
        String feature_name = eNode.getAttribute("name");
        boolean feature_abstract = false;
        boolean feature_mandatory = false;

        if (eNode.getAttribute("mandatory").equals("true"))
            feature_mandatory = true;
        if (eNode.getAttribute("abstract").equals("true"))
            feature_abstract = true;

        feature.setName(feature_name);
        feature.setAbstract(feature_abstract);
        feature.setMandatory(feature_mandatory);
        feature.setNumber(featureNumber);
        featureNumber++;
    }
}