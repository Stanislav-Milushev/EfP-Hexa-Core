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

public class EMFToCNFParser {

	/**
	 * TODO: Create CNF-Formula out of Feature Tree (No need for creating own data
	 * structures for CNF, it can be stored e.g. as a String) No Transformation
	 * needed, formula can be directly translated into CNF * Note: Please use the
	 * following annotation for representing logical operators OR: | AND: &
	 * Negation: ~ Implies: => Equals: <=>
	 *
	 */

	final static String SPACE = " ";
	final static String OPEN = "(";
	final static String CLOSE = ")";
	final static String OR = " | ";
	final static String AND = " & ";
	final static String NEGATION = "~";
	final static String IMPLIES = " => ";

	/*
	 * this Implementation/ methods is incomplete. it just take the first children
	 * of the root.
	 * 
	 * 
	 */

	public ArrayList<String> parse(FeatureModel featureModel) {
//		ArrayList<String> parseResult = new ArrayList<>();
//		
//		ArrayList<ArrayList<String>> Result = new ArrayList<>();
//		
//		ArrayList<Feature> getRootChildren =new ArrayList<Feature>();
//		
//		String featureName = featureModel.getRoot().getName();
//		EList<Feature> elist = featureModel.getRoot().getChildren();
//		elist.forEach((feature) -> getRootChildren.add(feature));
//		
//		Result=generateAllGroupTypeList(getRootChildren, featureName);
//		
//		for (int i = 0; i < Result.size(); i++) {
//
//			
//			EList<Feature>	rootchildren = featureModel.getRoot().getChildren();
//			getRootChildren.addAll(rootchildren);
//			
//			
//			Result=generateAllGroupTypeList(getRootChildren, featureName);
//			
//					for (int j = 0; j < Result.size(); j++)
//					{		
//						parseResult.addAll(Result.get(j));
//					}
//		}
//			
//		return parseResult;		
		
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

	
	private ArrayList<String> parseRoot(Feature rootFeature){
		ArrayList<String> result=new ArrayList<>();
		
		result.add(OPEN);
//		result.add(""+rootFeature.getNumber());
		result.add(""+rootFeature.getName());
		result.add(CLOSE);
		result.add(AND);
		
		return result;
	}
	
	
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
					// Alternative Variante mit getNumber() anstelle von getName()
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

	private ArrayList<String> parseOr(ArrayList<Group> orInput) {
		ArrayList<String> result = new ArrayList<>();
		for (Iterator<Group> orIterator = orInput.iterator(); orIterator.hasNext();) {
			Group group = (Group) orIterator.next();
			Feature rootFeature = group.getFeature().get(0);
			EList<Feature> featureList = group.getFeature();
			
//			result.add(OPEN);
//			result.add("" + rootFeature.getNumber());
			result.add("" + rootFeature.getName());
//			result.add(CLOSE);
			
			result.add(IMPLIES);
//			result.add(OPEN);

			for (int i = 1; i < featureList.size(); i++) {
//				result.add(OPEN);
//				result.add(""+featureList.get(i).getNumber());
				result.add(""+featureList.get(i).getName());
//				result.add(CLOSE);
				
				result.add(OR);
			}
			result.remove(result.size() - 1);
//			result.add(CLOSE);
			result.add(AND);
		}
		
		//result.add("\n");
		return result;
	}

	/*
	 * This method takes as parameter The name of the parent and a list of all of
	 * his children, then the method split/classified each of the children according
	 * to their Group type or the relation they have with the parent (Or group/ ALt
	 * / Mandatory).
	 * 
	 * once the separation is done, they are been send to their respective method:
	 * ALtparse method or Mandatoryparse or Orparse methods or Hierarchyparse
	 * 
	 * three result is obtained
	 * 
	 */
	public ArrayList<ArrayList<String>> generateAllGroupTypeList(ArrayList<Feature> getRootChildren,
			String featureName) {
		ArrayList<ArrayList<String>> Result = new ArrayList<>();
		ArrayList<Feature> ALTGroup = new ArrayList<>();
		ArrayList<Feature> ORGroup = new ArrayList<>();
		ArrayList<Feature> Mandatory = new ArrayList<>();
		ArrayList<Feature> Optional = new ArrayList<>();
		ArrayList<Feature> Hierarchy = new ArrayList<>();

		for (Feature feature : getRootChildren) {
			if (feature.getGroup() != null) {
				if (feature.getGroup().getGroupType().equals(GroupType.ALT)) {
					ALTGroup.add(feature);
				} else if (feature.getGroup().getGroupType().equals(GroupType.AND)) {

				} else if (feature.getGroup().getGroupType().equals(GroupType.OR)) {
					ORGroup.add(feature);
				} else {
				}
			} else {
				if (feature.isMandatory()) {
					Mandatory.add(feature);
				} else {
					Optional.add(feature);
				}
			}

		}

		Result.add(Alternativeparse(ALTGroup, featureName));
		Result.add(Orparse(ORGroup, featureName));
		Result.add(Mandatoryparse(Mandatory, featureName));
		Result.add(Hierarchyparse(Hierarchy, featureName));

		return Result;

	}

	// Mandatory CNF methods
	// FeatureName is the name of the Parent
	// ArrayList of feature ( children of the parent)

	public ArrayList<String> Mandatoryparse(ArrayList<Feature> features, String featureName) {
		int Lastindex = features.size();
		String Mandatory = featureName + IMPLIES + AND;
		ArrayList<String> MandatoryResult = new ArrayList<String>();

		for (Feature feature : features) {

			if (features.size() != Lastindex)
				Mandatory = Mandatory + featureName + IMPLIES + feature.getName() + AND;
			else {
				Mandatory = Mandatory + featureName + IMPLIES + feature.getName();
			}

			MandatoryResult.add(Mandatory);

		}

		return MandatoryResult;
	}

	// Hierarchy CNF methods
	// FeatureName is the name of the Parent
	// ArrayList of feature ( children of the parent)

	public ArrayList<String> Hierarchyparse(ArrayList<Feature> features, String featureName) {
		int Lastindex = features.size();
		String Hierarchy = featureName + IMPLIES + AND;
		ArrayList<String> HierarchyResult = new ArrayList<String>();

		for (Feature feature : features) {

			if (features.size() != Lastindex)
				Hierarchy = Hierarchy + feature.getName() + IMPLIES + featureName + AND;
			else {
				Hierarchy = Hierarchy + feature.getName() + IMPLIES + featureName;
			}

			HierarchyResult.add(Hierarchy);

		}

		return HierarchyResult;
	}

	// alternative CNF methods
	// FeatureName is the name of the Parent
	// ArrayList of feature ( children of the parent)

	public ArrayList<String> Alternativeparse(ArrayList<Feature> features, String featureName) {
		String ALternative = featureName + IMPLIES + OPEN + OPEN;
		int Lastindex = features.size();
		ArrayList<String> AlternativeResult = new ArrayList<String>();

		for (Feature feature : features) {

			for (Feature feature2 : features) {

				if (feature.equals(feature2)) {
					ALternative = ALternative + feature2.getName();
				} else if (features.size() == Lastindex) {
					ALternative = ALternative + NEGATION + feature2.getName();
				} else {
					ALternative = ALternative + NEGATION + feature2.getName() + AND;
				}
			}

			if (features.size() == Lastindex)
				ALternative = ALternative + CLOSE + CLOSE;
			else {
				ALternative = ALternative + CLOSE + OR + OPEN;
			}
			AlternativeResult.add(ALternative);

		}

		return AlternativeResult;

	}

	// OR CNF methods
	// FeatureName is the name of the Parent
	// ArrayList of feature ( children of the parent)

	public ArrayList<String> Orparse(ArrayList<Feature> features, String featureName) {
		String Or = featureName + IMPLIES + OPEN + OPEN;
		int Lastindex = features.size();
		ArrayList<String> OrResult = new ArrayList<String>();

		for (Feature feature : features) {

			for (Feature feature2 : features) {

				if (feature.equals(feature2)) {
					Or = Or + feature2.getName();
				} else if (features.size() == Lastindex) {
					Or = Or + feature2.getName();
				} else {
					Or = Or + feature2.getName() + OR;
				}
			}

			if (features.size() == Lastindex)
				Or = Or + CLOSE + CLOSE;
			else {
				Or = Or + CLOSE + AND + OPEN;
			}
			OrResult.add(Or);

		}

		return OrResult;

	}

}
