package org.mbe.sat.assignment;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

public class PairWiseSampler {
	List<Set<Variable>> allPairs = new LinkedList<Set<Variable>>();
	HashSet<Assignment> allValidSchemas = new HashSet<Assignment>();
	CnfFormula formula;
	SolutionSimplifier simp = new SolutionSimplifier();
	
	public PairWiseSampler(CnfFormula formula){
		this.formula = formula;
		createPairs();
		createAllValidSchemas();
	}
	
	
	private void createPairs() {
		Set<Variable> allVars = this.formula.getVariables();
		allVars.forEach(x -> {
			allVars.forEach(y-> {
				if(!x.getName().equals(y.getName()) && !pairExists(x, y)) {
					HashSet<Variable> pair = new HashSet<Variable>();
					pair.add(x);
					pair.add(y);
					allPairs.add(pair);
				}
			});
		});
	}
	
	
	private boolean pairExists(Variable var1, Variable var2) {
		return allPairs.stream().anyMatch(x -> {
			return x.contains(var2) && x.contains(var1);
		});
	}
	
	private void createAllValidSchemas(){
		allPairs.forEach(pair -> {
			int index = 0;
			while(index < 4) {
				Assignment currentAssignment = assignPair(pair, index);
				CnfFormula simpForm = simp.simplify(formula, currentAssignment);
				
				if(!simpForm.containsEmptyClause()) {
					allValidSchemas.add(currentAssignment);
				}
				index++;
			}
		});
	}
	
	private Assignment assignPair(Set<Variable> pair, int index) {
		Assignment ass = new Assignment();
		Variable[] vars = pair.stream().toArray(Variable[]::new);
		
		switch(index) {
		case 0: 
			ass.setValue(vars[0], false);
			ass.setValue(vars[1], false);
			break;
		case 1: 
			ass.setValue(vars[0], false);
			ass.setValue(vars[1], true);
			break;
		case 2: 
			ass.setValue(vars[0], true);
			ass.setValue(vars[1], false);
			break;
		case 3: 
			ass.setValue(vars[0], true);
			ass.setValue(vars[1], true);
			break;
		}
		
		return ass;
	}
	
	private void filterPairs(){
		/*
		allPairs = allPairs.stream().filter(pair -> {
			boolean checkTRUETRUE = false, checkTRUEFALSE = false, checkFALSETRUE = false, checkFALSEFALSE = false;
			Assignment ass = new Assignment();
			pair.forEach(variable -> {
				ass.setValue(variable, true);				
			});
			
			// TRUE TRUE
			SolutionSimplifier simp = new SolutionSimplifier();
			CnfFormula simplifiedFormula = simp.simplify(formula, ass);
			checkTRUETRUE = simplifiedFormula.getClauses().stream().anyMatch(clause -> clause.getLiterals().isEmpty());
			
						
			return checkTRUETRUE && checkTRUEFALSE && checkFALSETRUE && checkFALSEFALSE;
		}).toList();*/
	}
	
}
