package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DpllSolver implements ISolver<CnfFormula, Optional<Assignment>> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DpllSolver.class);

    protected ISimplifier<CnfFormula, Assignment> simplifier;

    private boolean terminate;
    
    public static Set<Variable> allVariables;
    
    /**
     * Creates a new instance of {@link DpllSolver}.
     */
    public DpllSolver() {
        // Since this assignment depends on assignment 2, you may choose to
        // use your own simplifier instead of the one from the sample solution
        this.simplifier = new SolutionSimplifier();
        // this.simplifier = new YourSimplifier();
        this.terminate=false;
    }

    /**
     * Solves the given CNF formula by finding a satisfying assignment using a recursive decision tree. Partial
     * assignments that can no longer lead to a satisfying assignment, will be skipped.
     *
     * @param cnfFormula the formula that is solved
     * @return an optional containing the satisfying assignment, or an empty optional, if the formulas is not
     * satisfiable
     */
    @Override
    public Optional<Assignment> solve(CnfFormula cnfFormula) {
        LOG.debug("Solving formula '{}' using '{}'", cnfFormula, getClass().getSimpleName());
        allVariables = cnfFormula.getVariables();
        ////////////////
        Assignment assignment = new Assignment();
		Optional<Assignment> result = DPLLAlgorithmus(cnfFormula, assignment);
        ////////////////
        
        this.terminate = false;
        if(!result.isPresent()) {
        	return Optional.empty();
        }
        return result;
    }

	/**
	 * DP-Algorithm. Recursive iteration through the decision-tree until a suitable
	 * solution was found. Cancels decision paths that cant satisfy the formula.
	 * 
	 * @param cnfFormula the formula that is solved
	 * @param assignment the current assignment => path of decision tree
	 * @return an optional containing the satisfying assignment, or an empty
	 *         optional, if the formulas is not satisfiable
	 */
	private Optional<Assignment> DPLLAlgorithmus(CnfFormula cnfFormula, Assignment assignment) {
		if (terminate) {
			return Optional.empty();
		}
		
		CnfFormula tempFormula = new CnfFormula(cnfFormula);
		Optional<Assignment> assPLE = new PureLiteralElimination(simplifier).ple(tempFormula);

		tempFormula = new CnfFormula(cnfFormula);
		Optional<Assignment> assUP = new UnitPropagation(simplifier).up(tempFormula);

		if(!assUP.equals(Optional.empty())) {
			for (Iterator<Variable> iterator = assUP.get().getVariables().iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				assignment.setValue(variable, assUP.get().getValue(variable).isTrue());
			}
		}
		if(!assPLE.equals(Optional.empty())) {
			for (Iterator<Variable> iterator = assPLE.get().getVariables().iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				assignment.setValue(variable, assPLE.get().getValue(variable).isTrue());
			}
		}

		cnfFormula = simplifier.simplify(cnfFormula, assignment);
		CnfFormula cnfFormulaSimplified = simplifier.simplify(cnfFormula, assignment);

		if (hasEmptyClause(cnfFormulaSimplified)) {
			return Optional.empty();
		} else if (cnfFormulaSimplified.getOperands().size() < 1) {		
			fillUnusedVariablesForSatisfyingAssignment(assignment, cnfFormula);
			return Optional.of(assignment);
		}
		
		Variable nextUnusedVariable = getNextUnusedVariable(cnfFormula, assignment);
		if (nextUnusedVariable == null) { return Optional.of(assignment); }
		 
		Assignment nextTrue = assignment; 
		Assignment nextFalse = cloneAssignment(assignment);
		 
		nextTrue.setValue(nextUnusedVariable, true);
		nextFalse.setValue(nextUnusedVariable, false);

		
		Optional<Assignment> returnValue = DPLLAlgorithmus(cnfFormula, nextTrue);

		return !returnValue.isPresent() ? DPLLAlgorithmus(cnfFormula, nextFalse) : returnValue;
	}
    
	
	/**
	 * Fills unused variables (not assigned) to an assignment that satisfies the
	 * formula.
	 * 
	 * @param ass the assignment that needs to be filled
	 * @param cnf the formula that contains all variables
	 */
	private void fillUnusedVariablesForSatisfyingAssignment(Assignment ass, CnfFormula cnf) {
		Iterator<Variable> it = allVariables.iterator();
		while (it.hasNext()) {
			Variable current = it.next();
			if (ass.getValue(current) == Tristate.UNDEFINED) {
				ass.setValue(current, true);
			}
		}
	}

	/**
	 * Clones an assignemt to a new object.
	 * 
	 * @param assignment the assignment that needs to be cloned
	 * @return new assignment object that equals the parameter.
	 */
	private Assignment cloneAssignment(Assignment assignment) {
		Assignment newAssignment = new Assignment();
		Iterator<Variable> assignmentIterator = assignment.getVariables().iterator();
		while (assignmentIterator.hasNext()) {
			Variable currentVariable = assignmentIterator.next();
			newAssignment.setValue(currentVariable, assignment.getValue(currentVariable).isTrue());
		}
		return newAssignment;
	}

	/**
	 * Gets an unused variable. if none was found => returns null
	 * 
	 * @param cnfFormula the formula that contains all variables
	 * @param assignment the assignment with all used variables
	 * @return a unused variable or null
	 */
	private Variable getNextUnusedVariable(CnfFormula cnfFormula, Assignment assignment) {
		Iterator<Variable> variableIterator = allVariables.iterator();
		while (variableIterator.hasNext()) {
			Variable currentVariable = variableIterator.next();
			if (!hasVariableAssigned(currentVariable, assignment)) {
				return currentVariable;
			}
		}

		
		return null;
	}

	/**
	 * Checks if a variable is assigned to given assignment. Function is used by
	 * getNextUnusedVariable(...).
	 * 
	 * @param variable   that needs to be checked
	 * @param assignment the assignment with all used variables
	 * @return true if variable is already in the assignment, else false
	 */
	private boolean hasVariableAssigned(Variable variable, Assignment assignment) {
		Iterator<Variable> assignedVariablesIterator = assignment.getVariables().iterator();
		while (assignedVariablesIterator.hasNext()) {
			if (assignedVariablesIterator.next().equals(variable)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a given formula contains an empty clause
	 * 
	 * @param cnfFormula that needs to be checked
	 * @return true if there is an empty clause in the formula, else false
	 */
	private boolean hasEmptyClause(CnfFormula cnfFormula) {
		Set<Or<Atom>> clausesWithBottom = cnfFormula.getOperands().stream()
				// Remove clauses that contain a TRUE constant
				.filter(this::containsEmptyClause).collect(Collectors.toSet());
		return !clausesWithBottom.isEmpty();
	}

	/**
	 * Checks if given clause is empty. function used by hasEmptyClause(...).
	 * 
	 * @param clause that needs to be checked
	 * @return true if the clause is empty, else false
	 */
	private boolean containsEmptyClause(Or<Atom> clause) {
		return clause.getOperands().isEmpty();
	}
	
	@Override
	public void terminate(boolean choice) {
		this.terminate=choice;
	}

	@Override
	public boolean isTerminated() {
		return this.terminate;
	}
}
