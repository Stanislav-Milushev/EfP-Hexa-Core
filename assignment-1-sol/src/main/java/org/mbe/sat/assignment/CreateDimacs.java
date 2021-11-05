package org.mbe.sat.assignment;

import metaFeatureModel.Feature;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeSet;

public class CreateDimacs {
	private String dimacsPreamble;
	private ArrayList<String>dimacsTree;
	private ArrayList<String>dimacsCrossTree;
	private TreeSet<String> dimacsFeatureComment;
	private Feature root;
	private String uri;
	private String cnfTree;
	private String cnfCrossTree;
	
	public CreateDimacs(Feature root, String cnfTree, String cnfCrossTree, String uri){
		this.root = root;
		this.uri= uri;
		this.cnfTree = cnfTree;
		this.cnfCrossTree = cnfCrossTree;
		this.dimacsCrossTree = new ArrayList<>();
		this.dimacsTree = new ArrayList<>();
		this.dimacsFeatureComment = new TreeSet<>();

		init();
	}
	
	public void init() {
		
		dimacsFeatureComment = new TreeSet<>();
		dimacsFeatureComment.add("c " + root.getNumber() + " " + root.getName());
		createDimacsFormat(this.cnfTree, dimacsTree);
		createDimacsFormat(this.cnfCrossTree, dimacsCrossTree);
		createDimacsComment(root);
		createDimacsPreamble();
		createDimacsFile();
		System.out.println("Dimacs File initialized...");
	}
	
	public void createDimacsPreamble() {
		int dimacsSize= dimacsTree.size()+ dimacsCrossTree.size();
		dimacsPreamble = "p cnf " + dimacsFeatureComment.size() + " " + dimacsSize;
	}
	
	public void createDimacsComment(Feature f) {
		if (f.getChildren().size() != 0) {
			for (int i = 0; i < f.getChildren().size(); i++) {
				dimacsFeatureComment.add("c " + f.getChildren().get(i).getNumber() + " " + f.getChildren().get(i).getName());
			}
			for (int i = 0; i < f.getChildren().size(); i++) {
				createDimacsComment(f.getChildren().get(i));
			}
		}
	}
	
	public void createDimacsFile() {
		System.out.println(uri);

		String filename = new File(uri).getName();
		System.out.println("filename: " + filename);
		uri = filename.replace(".xml", ".dimacs");

		String path = "src"+File.separator+"main"+File.separator+"resources" + File.separator+uri;
		File dimacsFile = new File(path);
		try {
			dimacsFile.createNewFile();
			PrintWriter pw = new PrintWriter(dimacsFile);
		
			//Writes Comment Preamble into a new File
			for (String x : dimacsFeatureComment) {
				pw.println(x);
			}
			pw.println(dimacsPreamble);

			//Dimacs standard tree
			for (String dt : dimacsTree) {
					pw.println(dt+" 0");
			}

			//Dimacs cross tree
			for (String dct: dimacsCrossTree) {
				pw.print(dct+" 0");
				pw.println();
			}

			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createDimacsFormat(String formula, ArrayList<String > cnf){
			for (String dimacs : formula.split("&")) {
				String tmpDimacs = dimacs.replace(" ","").replace("|", " ").replace("(", "").replace(")", "").replace("~", "-");
				cnf.add(tmpDimacs);
			}
	}
}
