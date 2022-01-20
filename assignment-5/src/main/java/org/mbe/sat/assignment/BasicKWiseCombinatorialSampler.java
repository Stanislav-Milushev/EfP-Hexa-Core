package org.mbe.sat.assignment;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mbe.assignment.sat.DpllSolver;
import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.solver.BaseSolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.mbe.sat.impl.solver.SolutionDpSolver;

public class BasicKWiseCombinatorialSampler implements ISampler<CnfFormula, Assignment> {

	/**
	 * Der Solver, der für das Verfahren verwendet werden soll.
	 */
	private SelectedSolver selectedSolver=SelectedSolver.DPLL;

    @Override
    public Set<Assignment> sample(CnfFormula formula) {
    	// wir klonen die formula um die eigentliche Formula durch Simplify-Prozesse nicht zu veraendern
    	CnfFormula cnfFormula = new CnfFormula(formula);
    	
    	PairWiseSampler pairWiseSampler = new PairWiseSampler(cnfFormula);
    	HashSet<Assignment> allValidSchemas = pairWiseSampler.allValidSchemas;

    	// Checken ob Paare vorhanden sind
    	if(allValidSchemas==null || allValidSchemas.isEmpty()) {
    		return new HashSet<>();
    	}
    	
    	Set<Assignment> allFullAssignments = new HashSet<>(); // die rueckgabe-liste
    	
    	allValidSchemas.stream().forEach(pair -> { // fuer jedes valide Paar...
    		Assignment assignment = new Assignment(pair); // Neues Assignment erstellen mit dem Paar initialisiert
    		CnfFormula clonedFormula = new CnfFormula(formula); // Klonen der Formula
    		
    		allValidSchemas.stream().forEach(pair2 -> { // durch alle validen Paare gehen...
    			List<Variable> pair2Vars = pair2.getVariables();
    			// Wenn variablen von pair2 noch nicht im Assignment vorhanden sind sind...
    			if(!pair2Vars.stream().anyMatch( p2var -> assignment.getVariables().contains(p2var))) {
    				assignment.merge(pair2); // Dem assignment das Paar hinzufuegen
    			}
    		});
    		
    		// Formula mit dem momentanen Assignment simplifyen und solven
			SolutionSimplifier simp = new SolutionSimplifier();
			CnfFormula form = simp.simplify(clonedFormula, assignment);
			Optional<Assignment> solution = getSelectedSolver().solve(form);
			
			if(solution.isPresent()) { // falls die forumla mit dem momentanen assignment solvable ist
    			allFullAssignments.add(assignment); // zu der rueckgabe-liste adden
			}
        });
    	
    	
    	return allFullAssignments;
    }

    /**
     * Gets an Solver Object of the selected Solver
     * @return Instance of ISovler Object
     */
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

	public void selectSolver(SelectedSolver selectedSolver) {
    	if(selectedSolver!=null) {
    		this.selectedSolver=selectedSolver;
    	}
    }
}

enum SelectedSolver{
	BASE,DP,DPLL
}