package org.mbe.sat.assignment;
import metaFeatureModel.FeatureModel;
import metaFeatureModel.Feature;
import metaFeatureModel.GroupType;

import java.util.ArrayList;

public class EMFToCNFParser {
    private String cnfFormula;

    public String parse (FeatureModel featureModel){
        ArrayList<Integer> check_feature = new ArrayList<>();
        cnfFormula = featureModel.getRoot().getNumber() + " & "; //Initialize Root for Formula
        loopFeatureTree(featureModel.getRoot(), check_feature);
        cnfFormula = cnfFormula.substring(0, cnfFormula.length() -2); // String always contains two extra chars at the end which need to be removed
        return cnfFormula;
    }


    //First Step -> Loop through Children of root and receive a formula
    private void loopFeatureTree(Feature root, ArrayList<Integer> checkFeature){
        for (Feature child: root.getChildren()){
            if(!checkFeature.contains(root.getNumber())) {
                createFeatureFormula(root);
                checkFeature.add(root.getNumber());
            }
            loopFeatureTree(child, checkFeature);
        }
    }


    //Creates Formula for parents after checking Group
    private void createFeatureFormula(Feature parent){
        if(parent.getGroup().getGroupType().equals(GroupType.AND)){
            andGroup(parent);
        }
        else if (parent.getGroup().getGroupType().equals(GroupType.OR)){
            orGroup(parent);
        }else if (parent.getGroup().getGroupType().equals(GroupType.ALT)){
            altGroup(parent);
        }
        else {
            System.out.println("GroupType not found of: "+parent.getNumber());
        }
    }


    private void andGroup(Feature parent) {
        for (Feature children : parent.getChildren()) {
            if (children.isMandatory()) {
                cnfFormula += "(~" + children.getNumber() + " | " + parent.getNumber() + ")"
                                + " & (~" + parent.getNumber() + " | " + children.getNumber() + ") & ";
            }
            else
                cnfFormula += "("+parent.getNumber() + " | " + "~"+children.getNumber() + ") & ";
        }
    }

    private void orGroup(Feature parent){
        String tmp_Or_Formula = "";
        for (Feature children: parent.getChildren()){
            cnfFormula += children.getNumber()+" | ";
            tmp_Or_Formula += "(~"+children.getNumber()+" | "+ parent.getNumber()+") & ";
        }
        cnfFormula = cnfFormula.substring(0, cnfFormula.length() -2);
        cnfFormula += " | ~"+parent.getNumber()+" & ";
        cnfFormula += tmp_Or_Formula;
    }

    private void altGroup(Feature parent){
        for (int i = 0; i < parent.getChildren().size(); i++){
            for (int j = i+1; j < parent.getChildren().size(); j++){
                cnfFormula += "(~" + parent.getChildren().get(i).getNumber()+ " | "+ "~" + parent.getChildren().get(j).getNumber()+ ") & ";
            }
        }
        orGroup(parent);
    }
}
