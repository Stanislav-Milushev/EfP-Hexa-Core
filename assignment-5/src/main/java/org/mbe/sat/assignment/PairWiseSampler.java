package org.mbe.sat.assignment;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

/**
 * The Class PairWiseSampler.
 */
public class PairWiseSampler {
	
	/** The all pairs. */
	List<Set<Variable>> allPairs = new LinkedList<Set<Variable>>();
	
	/** The all valid schemas. */
	HashSet<Assignment> allValidSchemas = new HashSet<Assignment>();
	
	/** The formula. */
	CnfFormula formula;
	
	/** The simp. */
	SolutionSimplifier simp = new SolutionSimplifier();
	
	/**
	 * Instantiates a new pair wise sampler.
	 *
	 * @param formula the formula
	 */
	public PairWiseSampler(CnfFormula formula){
		this.formula = formula;
		createPairs();
		createAllValidSchemas();
	}
	
	
	/**
	 * Creates the pairs.
	 */
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
	
	
	/**
	 * Pair exists.
	 *
	 * @param var1 the var 1
	 * @param var2 the var 2
	 * @return true, if successful
	 */
	private boolean pairExists(Variable var1, Variable var2) {
		return allPairs.stream().anyMatch(x -> {
			return x.contains(var2) && x.contains(var1);
		});
	}
	
	/**
	 * Creates the all valid schemas. (valid pairs)
	 */
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
	
	/**
	 * Assign pair.
	 *
	 * @param pair the pair
	 * @param index the index
	 * @return the assignment
	 */
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
	
}
