package org.mbe.sat.assignment;

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

/**
 * Creates CNF-Formulas from CrossTree-Features, no need to change something
 * CNF-Formulas are saved as a Strings in an ArrayList
 */

public class CrossTreeParser {

	private Document doc;
	private String logicalFormula = "";
	private final FormulaFactory f = new FormulaFactory();
	private final PropositionalParser p = new PropositionalParser(f);
	private final String marker = "marked";
	private ArrayList<Feature> featureList;
	private String cnfCrossTree;
	private int bracketCounter;

	public CrossTreeParser(String uri,  FeatureModel featureModel) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(uri);

			cnfCrossTree = "";
			bracketCounter = 0;

			//Init featureList for names + numbers
			this.featureList = new ArrayList<>();
			this.featureList.add(featureModel.getRoot());
			createFeatureList(featureModel.getRoot());
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws ParserException {

		createConstraint();

		cnfCrossTree = cnfCrossTree.substring(0, cnfCrossTree.length() -1);

		System.out.println("CrossTree Constraints initialized...");
	}

	// Creates Constraints (Rules from XML)
	private void createConstraint() throws ParserException {
		NodeList constraints = doc.getElementsByTagName("constraints");
		Node constraint = constraints.item(0);

		for (int i = 0; i < constraint.getChildNodes().getLength(); i++) {
			if (constraint.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node start_Node = constraint.getChildNodes().item(i).getChildNodes().item(1);
				createNodes(start_Node);
				String tmp_logicalFormula = addStartBracket(bracketCounter) + logicalFormula;

				Formula formula = p.parse(tmp_logicalFormula);
				String cnf = formula.cnf().toString();
				cnfCrossTree += cnf + " & ";
				logicalFormula = "";
				bracketCounter = 0;
			}
		}
	}

	private void createNodes(Node node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node current_Node = node.getChildNodes().item(i);
			if (current_Node.getNodeType() == Node.ELEMENT_NODE) {
				if (current_Node.getNodeName().equals("var")) {
					if (node.getNodeName().equals("not")) {
						createLogicalFormulaNot(node, current_Node);
						node.setUserData(marker, marker, null);
					} else {
						createLogicalFormulaStandard( node, current_Node);
					}
					markParents(current_Node);
				}
				createNodes(current_Node);
			}
		}
	}

	private void createLogicalFormulaStandard(Node node, Node current_Node ){
		if (node.getChildNodes().item(1).equals(current_Node))
			logicalFormula += "("+getNodeNumber(current_Node.getTextContent());

		else if (node.getChildNodes().item(3) != null && node.getChildNodes().item(3).equals(current_Node))
			logicalFormula+=getNodeNumber(current_Node.getTextContent())+")";

	}

	private void createLogicalFormulaNot(Node node, Node current_Node){
		if (node.getParentNode().getChildNodes().item(1) != null && node.getParentNode().getChildNodes().item(1).equals(node))
			logicalFormula+="(" + syntaxFormatter(node.getNodeName())+ getNodeNumber(current_Node.getTextContent());

		else if (node.getParentNode().getChildNodes().item(3) != null && node.getParentNode().getChildNodes().item(3).equals(node))
			logicalFormula+= syntaxFormatter(node.getNodeName())+ getNodeNumber(current_Node.getTextContent()) +")";
	}

	private void markParents(Node node) {
		if (node.getParentNode().getUserData(marker) == null) {
			if (!node.getParentNode().getNodeName().equals("rule")) {
				logicalFormula += syntaxFormatter(node.getParentNode().getNodeName());
				node.getParentNode().setUserData(marker, marker, null);
			}
		} else {
			markParents(node.getParentNode());
		}
	}

	public String syntaxFormatter(String format) {
		if (!format.equals("not"))
		bracketCounter++;

		switch (format) {
			case "disj":
				return "|";
			case "conj":
				return "&";
			case "eq":
				return "<=>";
			case "not":
				return "~";
			default:
				return "=>";
		}
	}

	private void createFeatureList(Feature f) {
		if (f.getChildren().size() != 0) {
			for (int i = 0; i < f.getChildren().size(); i++) {
				featureList.add(f.getChildren().get(i));
			}
			for (int i = 0; i < f.getChildren().size(); i++) {
				createFeatureList(f.getChildren().get(i));
			}
		}
	}

	// Returns nodeNumber for Dimacs-Processing
	private String getNodeNumber(String nodeName) {
		for (Feature feature : featureList) {
			if (feature.getName().equals(nodeName)) {
				return "" + feature.getNumber();
			}
		}
		return null;
	}

	private String addStartBracket(int bracketCounter){
		if (bracketCounter % 2 == 0)
		return "(";
		else
			return "";
	}

	public String getCnfCrossTree(){
		return cnfCrossTree;
	}
}