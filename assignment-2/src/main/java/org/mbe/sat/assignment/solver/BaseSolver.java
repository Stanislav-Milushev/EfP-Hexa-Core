package org.mbe.sat.assignment.solver;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.procedure.Simplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;



public class BaseSolver implements ISolver<CnfFormula, Optional<Assignment>> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BaseSolver.class);

    @Override
    public Optional<Assignment> solve(CnfFormula formula) {
        LOG.debug("Solving formula '{}' using '{}'", formula, getClass().getSimpleName());
            
        Set<Variable> variables = formula.getVariables();
        BooleanCombinator booleanCombinator = new BooleanCombinator(variables.size());
        Simplifier simplifier = new Simplifier();
        
        // Get boolean list with all possible boolean combinations 
      
        Assignment assignment = new Assignment();
        int numberOfCombinations = (int)Math.pow(2, variables.size());
        int index;
              
        for(int i = 0; i + 1 <= numberOfCombinations; i++ ) {
        	// Reset assignment and index
        	boolean[] combination = booleanCombinator.getCombinationByBitIndex(i);
        	assignment = Assignment.empty();
        	index = 0;
        	
        	// Loop through the formula variables and set Boolean values depending on current combination
        	for (Iterator<Variable> it = variables.iterator(); it.hasNext(); ) { Variable
       		 	v = it.next(); 
        		assignment.setValue(v , combination[index]); 
       		 	index++;
        	}
        	 
        	// If the simplify method returns an empty formula, Optional is returned with a satisfying assignment
        	if(simplifier.simplify(formula, assignment).isEmpty()) {
        		return Optional.of(assignment);
        	}
        	
        }
                
        // Return an empty Optional, if no satisfying assignment was found.
        return Optional.empty();
    }

}
