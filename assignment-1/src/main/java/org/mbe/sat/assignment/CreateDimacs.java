package org.mbe.sat.assignment;

import java.io.*;
import java.util.ArrayList;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.io.writers.FormulaDimacsFileWriter;

public class CreateDimacs {
		String fileNameDimacs;
		Formula formula ;
		String bigcnfs = "";
	public CreateDimacs(String fileName,String formula,ArrayList<String> featuretree) throws ParserException {
		this.formula = Formulagenerator(formula,featuretree);
		this.formula= this.formula.cnf();
		this.fileNameDimacs = fileName;
    }
	
	public void WriteDimacs() {
		try {
			FormulaDimacsFileWriter.write("src/main/resources/"+fileNameDimacs, formula, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Formula Formulagenerator(String formula,ArrayList<String> featuretree){
		FormulaFactory f=new FormulaFactory();
		PropositionalParser p=new PropositionalParser(f);
		featuretree.forEach((feature)-> bigcnfs += feature);
		bigcnfs= bigcnfs + " & "+ formula ;
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
