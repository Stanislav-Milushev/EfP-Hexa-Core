package org.mbe.sat.assignment;

import metaFeatureModel.Feature;
import metaFeatureModel.FeatureModel;
import metaFeatureModel.GroupType;

import java.util.ArrayList;
import java.util.Iterator;

import javax.management.openmbean.OpenDataException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EClassImpl.FeatureSubsetSupplier;

public class EMFToCNFParser {

    /**
     * TODO:
     *  Create CNF-Formula out of Feature Tree (No need for creating own data structures for CNF, it can be stored e.g. as a String)
     *  No Transformation needed, formula can be directly translated into CNF
     *       *
     * Note: Please use the following annotation for representing logical operators
     *  OR: |
     *  AND: &
     *  Negation: ~
     *  Implies: =>
     *  Equals: <=>
     *
     */

	String Space = " ";
	String BraketOp = "( ";
	String BraketCl = ") ";
	String OR = " | ";
	String AND = " & ";
	String Negation = " ~";
	String Implies = "=> ";

	
	/*
	 * this Implementation/ methods is incomplete. it just take the first children of the root.
	 * 
	 * 
	 * */
	

	ArrayList<String> parse (FeatureModel featureModel)
	{
			String featureName = featureModel.getRoot().getName();
			ArrayList<String> parse = new ArrayList<>();
			ArrayList<ArrayList<String>> Result = new ArrayList<>();
			ArrayList<Feature> getRootChildren = new ArrayList<Feature>();
					
			
			EList<Feature>	rootchildren = featureModel.getRoot().getChildren();
			getRootChildren.addAll(rootchildren);
			
			
			Result=generateAllGroupTypeList(getRootChildren, featureName);
			
					for (int i = 0; i < Result.size(); i++)
					{		
						parse.addAll(Result.get(i));
					}
			
			return parse;		
		
	}
	
	
	
	
	/*  
	 * This method takes as parameter The name of the parent and a list of all of his children, 
	 * then the method split/classified each of the children according to their Group type
	 * or the relation they have with the parent (Or group/ ALt / Mandatory).
	 * 
	 * once the separation is done, they are been send to their respective method:
	 * ALtparse method or Mandatoryparse or Orparse methods or Hierarchyparse
	 * 
	 * three result is obtained
	 * 
	 *      */
	public ArrayList<ArrayList<String>> generateAllGroupTypeList(ArrayList<Feature> getRootChildren,  String featureName) 
	{
		ArrayList<ArrayList<String>> Result = new ArrayList<>();
		ArrayList<Feature> ALTGroup = new ArrayList<>();
		ArrayList<Feature> ORGroup = new ArrayList<>();
		ArrayList<Feature> Mandatory = new ArrayList<>();
		ArrayList<Feature> Optional = new ArrayList<>();
		ArrayList<Feature> Hierarchy = new ArrayList<>();
 
					for (Feature feature : getRootChildren) 
					{	
									if( feature.getGroup()!= null) 
									{
											if (feature.getGroup().getGroupType().equals(GroupType.ALT)) 
											{
												ALTGroup.add(feature);
											}	
									 		else if(feature.getGroup().getGroupType().equals(GroupType.AND)) 
									 		{
									 						
									 		}else if (feature.getGroup().getGroupType().equals(GroupType.OR))
									 		{
									 			ORGroup.add(feature);
									 		}
									 		else {}
						 			} 
									else 
						 			{
							 				if(feature.isMandatory()) 
							 				{
									 			Mandatory.add(feature);
							 				}
							 				else
							 				{
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

    public ArrayList<String> Mandatoryparse (ArrayList<Feature> features, String featureName)
    {
    	int  Lastindex = features.size();
    	String Mandatory = featureName + Implies + AND;
    	ArrayList<String> MandatoryResult = new ArrayList<String>(); 
    	
    	for (Feature feature : features) 
 	   {
					  
							
				if(features.size()!=Lastindex)
					Mandatory = Mandatory + featureName + Implies + feature.getName() + AND;
				else {
					Mandatory = Mandatory + featureName + Implies + feature.getName() ;
				}
		    		
			    MandatoryResult.add(Mandatory);
						
		  }
    	
           return MandatoryResult;
    }

    
    
    // Hierarchy CNF methods
    // FeatureName is the name of the Parent
    // ArrayList of feature ( children of the parent)

    public ArrayList<String> Hierarchyparse (ArrayList<Feature> features, String featureName)
    {
    	int  Lastindex = features.size();
    	String Hierarchy = featureName + Implies + AND;
    	ArrayList<String> HierarchyResult = new ArrayList<String>(); 
    	
    	for (Feature feature : features) 
 	   {
					  
							
				if(features.size()!=Lastindex)
					Hierarchy = Hierarchy + feature.getName()  + Implies + featureName + AND;
				else {
					Hierarchy = Hierarchy + feature.getName() + Implies + featureName ;
				}
		    		
			    HierarchyResult.add(Hierarchy);
						
		  }
    	
           return HierarchyResult;
    }
    
    
    
    // alternative CNF methods
    // FeatureName is the name of the Parent
    // ArrayList of feature ( children of the parent)
    
    public ArrayList<String>Alternativeparse(ArrayList<Feature> features, String featureName)
    {
	    	String ALternative = featureName + Implies + BraketOp + BraketOp;
	    	int  Lastindex = features.size();
	    	ArrayList<String> AlternativeResult = new ArrayList<String>();
	    
	    	for (Feature feature : features) 
	    	   {
						    		
			    		for (Feature feature2 : features)
			    			{
							
			    				if(feature.equals(feature2)) {  								
			    					ALternative = ALternative + feature2.getName();	    			 
				    			}else if(features.size()==Lastindex){
				    				ALternative = ALternative + Negation + feature2.getName(); 
				    			}
				    			else {
				    				ALternative = ALternative + Negation + feature2.getName() + AND ;
				    			}   		
			    			}	
			    		
				    			if (features.size()==Lastindex)
				    				ALternative = ALternative + BraketCl + BraketCl;
				    			else {
				    				ALternative = ALternative + BraketCl + OR + BraketOp;
				    			}
				    	AlternativeResult.add(ALternative);
								
			    }
	    	
	    		
    	return AlternativeResult;
      	
    }
    
    // OR CNF methods
    // FeatureName is the name of the Parent
    // ArrayList of feature ( children of the parent)
    
    
    public ArrayList<String>Orparse(ArrayList<Feature> features, String featureName)
    {
	    	String Or = featureName + Implies + BraketOp + BraketOp;
	    	int  Lastindex = features.size();
	    	ArrayList<String> OrResult = new ArrayList<String>();
	    
	    	for (Feature feature : features) 
	    	   {
						    		
			    		for (Feature feature2 : features)
			    			{
							
			    				if(feature.equals(feature2)) {  								
			    					Or = Or + feature2.getName();	    			 
				    			}else if(features.size()==Lastindex){
				    				Or = Or  + feature2.getName(); 
				    			}
				    			else {
				    				Or = Or + feature2.getName() + OR ;
				    			}   		
			    			}	
			    		
				    			if (features.size()==Lastindex)
				    				Or = Or + BraketCl + BraketCl;
				    			else {
				    				Or = Or + BraketCl + AND + BraketOp;
				    			}
				    	OrResult.add(Or);
								
			    }
	    	
	    		
    	return OrResult;
      	
    }
    
    


}
