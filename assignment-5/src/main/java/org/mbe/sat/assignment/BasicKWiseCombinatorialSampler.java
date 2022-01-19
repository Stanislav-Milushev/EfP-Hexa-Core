package org.mbe.sat.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mbe.assignment.sat.DpllSolver;
import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.solver.BaseSolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.mbe.sat.impl.solver.SolutionDpSolver;

public class BasicKWiseCombinatorialSampler implements ISampler<CnfFormula, Assignment> {

	private SelectedSolver selectedSolver=SelectedSolver.DPLL;

    @Override
    public Set<Assignment> sample(CnfFormula formula) {
    	PairWiseSampler pairWiseSampler = new PairWiseSampler(formula);
    	HashSet<Assignment> allValidSchemas = pairWiseSampler.allValidSchemas;

    	if(allValidSchemas==null || allValidSchemas.isEmpty()) {
    		return new HashSet<>();
    	}

    	Set<Assignment> sample=new HashSet<>();
    	List<Assignment> delList=new ArrayList<>();
    	ISolver<CnfFormula,Optional<Assignment>> solver=getSelectedSolver();
    	Assignment	testass= new Assignment();
    	Assignment newAssb = new Assignment();
    	Map<Variable,Boolean> setVarMapb=new HashMap<>();
    	Assignment ass = new Assignment();
    	
    	Set<Assignment> allFullAssignments = new HashSet<>();
    	allValidSchemas.stream().forEach(pair -> {
    		Assignment assignment = new Assignment(pair);
    		
    		allValidSchemas.stream().forEach(pair2 -> {
    			List<Variable> pair2Vars = pair2.getVariables();
    			// Wenn variablen von pair2 noch nicht im Assignment sind
    			if(!pair2Vars.stream().anyMatch( p2var -> assignment.getVariables().contains(p2var))) {
    				assignment.merge(pair2);
    			}
    		});
    		
			SolutionSimplifier simp = new SolutionSimplifier();
			CnfFormula form = simp.simplify(formula, assignment);
			Optional<Assignment> solution = getSelectedSolver().solve(form);
			
			if(solution.isPresent()) {
    			allFullAssignments.add(assignment);
			}
    		
        });
    	
    	
    	return allFullAssignments;
    }

    private boolean checkAssignmentFull(Assignment ass) {
		List<Variable> vars=ass.getVariables();
		
		//pr�fen, ob noch UNDEFINED Variablen vorliegen (falls nicht : counter=0)
		for (Iterator<Variable> varIterator = vars.iterator(); varIterator.hasNext();) {
			Variable variable = (Variable) varIterator.next();
			if(ass.getValue(variable).equals(Tristate.UNDEFINED)) {
				return false;
			}
		}
		return true;
	}

	private ISolver<CnfFormula, Optional<Assignment>> getSelectedSolver() {
		ISolver<CnfFormula,Optional<Assignment>> solver=null;

    	switch (this.selectedSolver) {
		case BASE:
			solver=new BaseSolver();
			break;

		case DP:
			solver=new SolutionDpSolver();
			break;

		case DPLL:
			solver=new DpllSolver();
			break;
		}
    	return solver;
	}

	private boolean assignmentFilled(Assignment assignment) {
		List<Variable> variables = assignment.getVariables();

		for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if(assignment.getValue(variable).equals(Tristate.UNDEFINED)) {
				return false;
			}
		}
		return true;
	}

	public void selectSolver(SelectedSolver selectedSolver) {
    	if(selectedSolver!=null) {
    		this.selectedSolver=selectedSolver;
    	}
    }
}

enum SelectedSolver{
	BASE,DP,DPLL
}