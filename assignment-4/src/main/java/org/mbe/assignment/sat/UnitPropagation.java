package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.procedure.IUnitPropagation;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Performs simplifications on formulas and clauses by propagating assignments for literals in clauses that only contain
 * one literal.
 */
public class UnitPropagation implements IUnitPropagation<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(UnitPropagation.class);
    
    private Set<Variable> allVariables;

    private final ISimplifier<CnfFormula, Assignment> simplifier;

    public UnitPropagation(ISimplifier<CnfFormula, Assignment> simplifier) {
        this.simplifier = simplifier;
    }

    /**
     * Performs the simplification procedure "Unit Propagation" (UP) on the given formula. This method works in-place
     * and modifies the given formula. UP searches for clauses that only contain one literal. In order to satisfy this
     * clause, the literal must evaluate to TRUE. So the variable is assigned a value to satisfy its clause and the
     * value is set (propagated) for all literals with the same variable in the whole formula.
     *
     * @param cnfFormula the formula that is simplified using UP
     * @return the assignment that contains the values that where set to the variables during UP
     */
    public Optional<Assignment> up(CnfFormula cnfFormula) {
    	allVariables = cnfFormula.getVariables();
        LOG.trace("Simplifying formula using unit propagation {}", cnfFormula);
		Assignment ass = new Assignment();
		// OLD
		/*
		for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			for (Iterator<Or<Atom>> opIterator = cnfFormula.getOperands().iterator(); opIterator.hasNext();) {
				Or<Atom> clause = opIterator.next();
				if(clause.getLiterals().size() == 1) {
					for (Iterator<Literal> litIterator = clause.getLiterals().iterator(); litIterator.hasNext();) {
						Literal literal = litIterator.next();
						Variable var = literal.getVariable();
						if(var.getName() == variable.getName()) {
							ass.setValue(var, literal.isPositive());
							cnfFormula.setOperands(simplifier.simplify(cnfFormula, ass).getOperands());
						}
					}
				}
			}
		}*/
		//NEW
		boolean formulaChanged = true;
		while(formulaChanged) {
			formulaChanged = false;
			for (Iterator<Or<Atom>> opIterator = cnfFormula.getOperands().iterator(); opIterator.hasNext();) {
				Or<Atom> clause = opIterator.next();
				if(clause.getLiterals().size() == 1) {
					Literal singleLiteral = clause.getLiterals().stream().findFirst().get();
					ass.setValue(singleLiteral.getVariable(), singleLiteral.isPositive());
					cnfFormula.setOperands(simplifier.simplify(cnfFormula, ass).getOperands());
					formulaChanged = true;
				}
			}
		}
		
		
		if(ass.getVariables().isEmpty()) {
	        return Optional.empty();
		}
		return Optional.of(ass);
    }
}
