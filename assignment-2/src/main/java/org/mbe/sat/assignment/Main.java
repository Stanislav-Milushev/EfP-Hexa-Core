package org.mbe.sat.assignment;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.mbe.sat.assignment.parser.DimacsParser;
import org.mbe.sat.assignment.solver.BaseSolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

public class Main {
	
	public static void main(String[]args){
		
		try {
			
			 URL url = Main.class.getClassLoader().getResource("refrigerator.cnf");
			 DimacsParser dimacsParser = new DimacsParser();			 
			 CnfFormula formula = dimacsParser.parse(url.openStream());
			 			 
			 BaseSolver baseSolver = new BaseSolver();
			 Optional<Assignment> passed = baseSolver.solve(formula);
			 		 
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}