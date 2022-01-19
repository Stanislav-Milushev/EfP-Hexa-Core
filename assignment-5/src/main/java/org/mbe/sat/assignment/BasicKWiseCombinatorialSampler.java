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
import org.mbe.sat.impl.solver.SolutionDpSolver;

public class BasicKWiseCombinatorialSampler implements ISampler<CnfFormula, Assignment> {

	private SelectedSolver selectedSolver=SelectedSolver.DP;

    @Override
    public Set<Assignment> sample(CnfFormula formula) {
    	PairWiseSampler pairWiseSampler = new PairWiseSampler(formula);
    	HashSet<Assignment> allValidSchemas = pairWiseSampler.allValidSchemas;

    	if(allValidSchemas==null) {
    		return new HashSet<>();
    	}

    	Set<Assignment> sample=new HashSet<>();
    	Assignment ass=new Assignment();
    	List<Assignment> delList=new ArrayList<>();
    	ISolver<CnfFormula,Optional<Assignment>> solver=getSelectedSolver();
    	Assignment	testass= new Assignment();
    	while(!allValidSchemas.isEmpty()) {
    		//Iteration über alle Value Schemas
        	for (Iterator<Assignment> iterator = allValidSchemas.iterator(); iterator.hasNext();) {
    			Assignment assignment = iterator.next();

        		if(checkAssignmentFull(ass)) {
        			//falls Assignment gefüllt : zu sample hinzufügen
        			sample.add(ass);
        			ass=new Assignment();
        		}

    			//Konfiguration mit Samples vergleichen
    			List<Variable> values=assignment.getVariables();
    			boolean schemaValid=true;
    			Map<Variable,Boolean> setVarMap=new HashMap<>();

    			
    			//Value Schema mit aktuellem Assignment vergleichen
    			for (Iterator<Variable> valIterator = values.iterator(); valIterator.hasNext();) {
    				Variable variable = valIterator.next();
    				
    				//falls true : Variable kann theoretisch eingefügt werden / Abbruch sonst
    				if(ass.getValue(variable).equals(Tristate.UNDEFINED) || ass.getValue(variable).equals(assignment.getValue(variable))) {
    					setVarMap.put(variable, Boolean.valueOf(assignment.getValue(variable).isTrue()));
    				}else {
    					schemaValid=false;
    					break;
    				}

    			}

    			//falls eine "unpassende Variable" in Value Schema enthalten - skip
    			if(!schemaValid) {
    				continue;
    			}

    			//falls alle Variablen "passend" - in Assignment einfügen
    			Assignment newAss=new Assignment(ass);
    			
    			for (Map.Entry<Variable, Boolean> entry : setVarMap.entrySet()) {
    				Variable key = entry.getKey();
    				Boolean val = entry.getValue();
    				
    				newAss.setValue(key, val.booleanValue());
    			}	

    			//Validität prüfen / SAT
    			CnfFormula simpForm = pairWiseSampler.simp.simplify(pairWiseSampler.formula, newAss);
    			Optional<Assignment> solve = solver.solve(simpForm);
    			
    			if(solve.equals(Optional.empty())) {
    				continue;
    			}else {
    				//falls Value Schema valide : im nächste Schritt in eigentliches Assignment einfügen & aus Value Schemas löschen
    				delList.add(assignment);
    				
    				for (Map.Entry<Variable, Boolean> entry : setVarMap.entrySet()) {
    					Variable key = entry.getKey();
    					Boolean val = entry.getValue();
    					
    					ass.setValue(key, val.booleanValue());
    				}
    				//Elemente aus Liste löschen
    				allValidSchemas.removeAll(delList);
    			}
    			
    			setVarMap.clear();
    			if(testass.equals(ass)) {
    				System.out.println("es ist gleich");
    			}
    		}
        	
        	
        	
        	//zweiter Fall : falls Assignment nicht komplett gefüllt aber bereits alle Value-Schemas durchgelaufen => zu Sample hinzufügen
			sample.add(ass);

    	}
    	
        return sample;
    }

    private boolean checkAssignmentFull(Assignment ass) {
		List<Variable> vars=ass.getVariables();
		
		//prüfen, ob noch UNDEFINED Variablen vorliegen (falls nicht : counter=0)
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