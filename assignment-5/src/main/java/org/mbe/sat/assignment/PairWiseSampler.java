package org.mbe.sat.assignment;

import java.util.HashMap;
import java.util.HashSet;

import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;

public class PairWiseSampler {
	
	HashMap<Variable, Variable> pairs = new HashMap<Variable, Variable>();
	HashSet<Assignment> allValidSchemas = new HashSet<Assignment>();
	CnfFormula formula;
	
	public PairWiseSampler(CnfFormula formula){
		this.formula = formula;
		createPairs();
		createAllValidSchemas();
	}
	
	private void createPairs() {
		
	}
	
	private void createAllValidSchemas() {
		
	}
	
}
