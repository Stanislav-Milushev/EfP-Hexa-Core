package org.mbe.sat.assignment;

import metaFeatureModel.Feature;
import metaFeatureModel.FeatureModel;
import metaFeatureModel.Group;
import metaFeatureModel.GroupType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.openmbean.OpenDataException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EClassImpl.FeatureSubsetSupplier;

/**
 * @author Momo Johnson
 *
 */
/**
 * @author Darwin Brambor
 *
 */
public class EMFToCNFParser {

	/**
	 * TODO: Create CNF-Formula out of Feature Tree (No need for creating own data
	 * structures for CNF, it can be stored e.g. as a String) No Transformation
	 * needed, formula can be directly translated into CNF * Note: Please use the
	 * following annotation for representing logical operators OR: | AND: &
	 * Negation: ~ Implies: => Equals: <=>
	 *
	 */
	
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String SPACE = " ";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String OPEN = "(";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String CLOSE = ")";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String OR = " | ";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String AND = " & ";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String NEGATION = "~";
	/**
	 * chars and strings for creation of the CNF-formula 
	 */
	final static String IMPLIES = " => ";

	
	/**
	 * This method takes as parameter FeatureModel. From the FeatureModel with the use Stream and the
	 *  to sorted put the different groups elements:(ALT,OR,AND,mandatory and optional).
	 *  root-feature	<== parseRoot Method
	 *  alt-groups 		<== parseAlt Method
	 *  or-groups 		<== parseOr Method
	 *  hierarchy-group <== parsehierarchy Method
	 *  mandatory-group <== parseMandatory Method
	 *  
	 *  @param featureModel - takes an feature model in form of an EMF-object and converts it into an ArrayList with type String
	 */
	public ArrayList<String> parse(FeatureModel featureModel) {
		
		ArrayList<String> resultList = new ArrayList<>();

		//adding root-feature
//		resultList.add("\n\n   ROOT : \n");
		resultList.addAll(this.parseRoot(featureModel.getRoot()));
		
		//adding alt-groups
//		resultList.add("\n\n   ALT : \n");
		Stream<Group> altStream = featureModel.getGroup().stream().filter(s -> s.getGroupType().equals(GroupType.ALT));
		ArrayList<Group> altInput = (ArrayList<Group>) altStream.collect(Collectors.toList());
		resultList.addAll(this.parseAlt(altInput));
		
		//adding or-groups
//		resultList.add("\n\n   OR : \n");
		Stream<Group> orStream = featureModel.getGroup().stream().filter(s -> s.getGroupType().equals(GroupType.OR));
		ArrayList<Group> orInput = (ArrayList<Group>) orStream.collect(Collectors.toList());
		resultList.addAll(this.parseOr(orInput));
		
		//adding hierarchy-group
//		resultList.add("\n\n   HIERARCHY : \n");
		resultList.addAll(this.parseHierarchy(featureModel.getRoot()));
		
		//adding mandatory-group
//		resultList.add("\n\n   MANDATORY : \n");
		resultList.addAll(this.parseMandatory(featureModel.getRoot()));
		
		if(resultList.get(resultList.size()-1).equals(AND)){
			resultList.remove(resultList.size()-1);
		}
		
		return resultList;
	}
	

	/**
	 *  parseRoot method = Root
	 *  this method takes an object of type Feature, which the root of the tree (Root feature).
	 *  
	 *  @param rootFeature of the underlying feature diagram
	 *  @return ArrayList of String name as "Result"
	 */
	private ArrayList<String> parseRoot(Feature rootFeature){
		ArrayList<String> result=new ArrayList<>();
		
		result.add(OPEN);
//		result.add(""+rootFeature.getNumber());
		result.add(""+rootFeature.getName());
		result.add(CLOSE);
		result.add(AND);
		
		return result;
	}
	
	
	
	/**
	 *  parseMandatory method = Mandatory relationship
	 *  this method takes an object of type Feature, which the root of the tree (Root feature).
	 *  
	 *  Each features of the tree and its children are being sorted out to form the mandatory relationship.
	 *  This is obtained through a recursive method implemented by the last For loop at the end of this method 
	 * 
	 *  @param rootFeature of the underlying feature diagram 
	 *  @return ArrayList of String name as "Result"
	 */	
	private ArrayList<String> parseMandatory(Feature rootFeature){
		ArrayList<String> result=new ArrayList<>();
		EList<Feature> childFeatures=rootFeature.getChildren();
		
		for (Iterator<Feature> childIterator = childFeatures.iterator(); childIterator.hasNext();) {
			Feature child = (Feature) childIterator.next();
			
			if(child.isMandatory()) {
				result.add(OPEN);
				//result.add(""+rootFeature.getNumber());
				result.add(""+rootFeature.getName());
//				result.add(CLOSE);
				result.add(IMPLIES);
//				result.add(OPEN);
				//result.add(""+child.getNumber());
				result.add(""+child.getName());
				result.add(CLOSE);
				result.add(AND);
			}
		}
		
		for (Iterator<Feature> childIterator = childFeatures.iterator(); childIterator.hasNext();) {
			Feature child = (Feature) childIterator.next();
			result.addAll(this.parseMandatory(child));
		}
		
		return result;
	}
	
	/**
	 *  parseHierarchy method = Hierarchy CNF methods
	 *  this method takes an object of type Feature, which the root of the tree (Root feature).
	 *  
	 *  Each features of the tree and its children are being sorted out to form the hierarchy_edge.
	 *  This is obtained through a recursive method implemented by the last For loop at the end of this method 
	 *  
	 *  @param rootFeature of the underlying feature diagram 
	 *  @return ArrayList of String name as "Result"
	 */
	private ArrayList<String> parseHierarchy(Feature rootFeature){
		ArrayList<String> result=new ArrayList<>();
		EList<Feature> childFeatures=rootFeature.getChildren();
		
		for (Iterator<Feature> childIterator = childFeatures.iterator(); childIterator.hasNext();) {
			Feature child = (Feature) childIterator.next();
			result.add(OPEN);
			//result.add(""+child.getNumber());
			result.add(""+child.getName());
//			result.add(CLOSE);
			result.add(IMPLIES);
//			result.add(OPEN);
			//result.add(""+rootFeature.getNumber());
			result.add(""+rootFeature.getName());
			result.add(CLOSE);
			result.add(AND);
		}
		
		for (Iterator<Feature> childIterator = childFeatures.iterator(); childIterator.hasNext();) {
			Feature child = (Feature) childIterator.next();
			result.addAll(this.parseHierarchy(child));
		}
		return result;
	}
	
	
	
	/**
	 *  parseAlt method = Altenative-Groupe CNF methods
	 * this method takes an ArrayList of Group with the "Grouptype ALT (Alt Group) with name altInput"
	 *  and
	 *  
	 *  @param altInput / an ArrayList storing all Group-Objects of the underlying feature model with GroupType ALT
	 *  @return ArrayList of String name as "Result"
	 */
	
	private ArrayList<String> parseAlt(ArrayList<Group> altInput) {
		ArrayList<String> result = new ArrayList<>();
		
		for (Iterator<Group> altIterator = altInput.iterator(); altIterator.hasNext();) {
			Group group = (Group) altIterator.next();
			Feature rootFeature = group.getFeature().get(0);
			EList<Feature> featureList = group.getFeature();
			
			// Alternative Variante mit getNumber() anstelle von getName()
			result.add(OPEN);
			//result.add("" + rootFeature.getNumber());
			result.add("" + rootFeature.getName());
//			result.add(CLOSE);
			// Alternative Variante mit getName() anstelle von getNumber()
			//result.add("" + rootFeature.getName());
			
			result.add(IMPLIES);
//			result.add(OPEN);

			for (int i = 1; i < featureList.size(); i++) {
				result.add(OPEN);

				for (int j = 1; j < featureList.size(); j++) {
					if (i != j) {
						result.add(NEGATION);
					}
//					Alternative Variante mit getNumber() anstelle von getName()
//					result.add(OPEN);
//					result.add("" + featureList.get(j).getNumber());
					result.add("" + featureList.get(j).getName());
//					result.add(CLOSE);
					// Alternative Variante mit getName() anstelle von getNumber()
					//result.add("" + featureList.get(i).getName());
					
					result.add(AND);
				}
				result.remove(result.size() - 1);
				result.add(CLOSE);
				result.add(OR);
			}
			result.remove(result.size() - 1);
			result.add(CLOSE);
			result.add(AND);
		}
		
		
		//result.add("\n");
		return result;
	}
	
	
	
	/**
	 *  parseOr method = Or-Group CNF 
	 *  this method takes an ArrayList of Group with the "Grouptype OR (Or Group)"  with name orInput
	 *  
	 *  @param orInput
	 *  @return ArrayList of String name as "Result"
	 */
	private ArrayList<String> parseOr(ArrayList<Group> orInput) {
		ArrayList<String> result = new ArrayList<>();
		for (Iterator<Group> orIterator = orInput.iterator(); orIterator.hasNext();) {
			Group group = (Group) orIterator.next();
			Feature rootFeature = group.getFeature().get(0);
			EList<Feature> featureList = group.getFeature();
			
			result.add(OPEN);
//			result.add("" + rootFeature.getNumber());
			result.add("" + rootFeature.getName());
			
			
			result.add(IMPLIES);
//			result.add(OPEN);

			for (int i = 1; i < featureList.size(); i++) {
//				result.add(OPEN);
//				result.add(""+featureList.get(i).getNumber());
				result.add(""+featureList.get(i).getName());
//			result.add(CLOSE);
				
				result.add(OR);
			}
			result.remove(result.size() - 1);
			result.add(CLOSE);
			result.add(AND);
		}
		
		//result.add("\n");
		return result;
	}

}
