package org.mbe.sat.assignment;

import java.io.*;
import java.util.ArrayList;

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
	 *                    separated cnf-formula without cross-tree edges / feature-tree
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * generates a final cnf-formula by merging the cross-tree- and feature-tree-formula
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
