package org.mbe.sat.assignment;

import java.io.*;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.io.writers.FormulaDimacsFileWriter;

public class CreateDimacs {
		String fileName1;
		Formula formula1 ;
	public CreateDimacs(String fileName,String formula) throws ParserException {
		this.formula1 = Formulagenerator(formula);
		this.formula1=formula1.cnf();
		this.fileName1 = fileName;
    }
	public void WriteDimacs() {
		try {
			FormulaDimacsFileWriter.write("src/main/resources/"+fileName1, formula1, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Formula Formulagenerator(String formula){
		FormulaFactory f=new FormulaFactory();
		PropositionalParser p=new PropositionalParser(f);
		
		try {
			return p.parse(formula);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
