package org.mbe.sat.assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.io.writers.FormulaDimacsFileWriter;

public class CreateDimacs {
	/**
	 * filename of dimacs file
	 */
	String fileNameDimacs;
	/**
	 * constraint formula / cross tree edges
	 */
	Formula formula;
	/**
	 * final cnf-formula
	 */
	String bigcnfs = "";

	/**
	 * constructor
	 * 
	 * @param fileName    of the target .cnf/dimacs-file
	 * @param formula     / constraint-formula / cross-tree formula
	 * @param featuretree / ArrayList-output of the EMFToCNF parser containing the
	 *                    separated cnf-formula without cross-tree edges /
	 *                    feature-tree
	 * @throws ParserException
	 */
	public CreateDimacs(String fileName, String formula, ArrayList<String> featuretree) throws ParserException {
		this.formula = Formulagenerator(formula, featuretree);
		this.formula = this.formula.cnf();
		this.fileNameDimacs = fileName;
	}

	/**
	 * writes final form of the cnf-formula into the given file
	 */
	public void WriteDimacs() {
		try {
			FormulaDimacsFileWriter.write("src/main/resources/" + fileNameDimacs, formula, true);

			File f1 = new File("src/main/resources/" + fileNameDimacs + ".map");
			Scanner sc = new Scanner(f1);
			ArrayList<String> VarMaping = new ArrayList();
			ArrayList<String> NewMaping = new ArrayList();
			while (sc.hasNextLine()) {
				VarMaping.add(sc.nextLine().toString().replaceAll("\\d", "").replace(";", ""));
			}

			for (int i = 0; i < VarMaping.size(); i++) {

				NewMaping.add("c " + (i + 1) + " " + VarMaping.get(i));
				System.out.println(NewMaping.get(i)); 
			}

			sc.close();
			File f2 = new File("src/main/resources/" + fileNameDimacs + "M" + ".cnf");
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File("src/main/resources/" + fileNameDimacs + "M" + ".cnf"), true));
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + fileNameDimacs + ".cnf"));
			String line = br.readLine();
			while (NewMaping.isEmpty()==false) {
				pw.println(NewMaping.get(0));
				NewMaping.remove(0);
			}
			while(line != null)
	        {
	            pw.println(line);
	            line = br.readLine();
	        }
			
	          
	        // closing resources
	        br.close();
	        pw.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * generates a final cnf-formula by merging the cross-tree- and
	 * feature-tree-formula
	 * 
	 * @param formula
	 * @param featuretree
	 * @return null if a parser exception occured
	 */
	private Formula Formulagenerator(String formula, ArrayList<String> featuretree) {
		FormulaFactory f = new FormulaFactory();
		PropositionalParser p = new PropositionalParser(f);
		featuretree.forEach((feature) -> bigcnfs += feature);
		bigcnfs = bigcnfs + " & " + formula;
		System.out.println(bigcnfs);

		try {
			return p.parse(bigcnfs);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
