package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Performs simplifications on formulas and clauses by removing clauses that
 * contain a literal which has the same polarisation in all clauses it occurs
 * in. This implementation uses a sequential approach, meaning that it processes
 * only one pure literal at a time. For each pure literal that is found,
 * {@link ISimplifier#simplify(Object, Object) simplify} is called to to
 * eliminate the pure literal.
 */
public class PureLiteralElimination implements IPureLiteralElimination<CnfFormula, Assignment> {

	/**
	 * Static logger instance for this class.
	 */
	public static final Logger LOG = LoggerFactory.getLogger(PureLiteralElimination.class);

	/**
	 * The simplifier that is used to eliminate the pure literals in the formula.
	 */
	private final ISimplifier<CnfFormula, Assignment> simplifier;

	/**
	 * Creates a new instance of the {@link PureLiteralElimination} with the given
	 * simplifier which is used to eliminate the pure literals that where found in
	 * the formula
	 *
	 * @param simplifier simplifier which is used to eliminate the pure literals
	 *                   that where found in the formula
	 */
	public PureLiteralElimination(ISimplifier<CnfFormula, Assignment> simplifier) {
		this.simplifier = simplifier;
	}

	/**
	 * Performs the simplification procedure "Pure Literal Elimination" (PLE) on the
	 * given formula. This method works in-place and modifies the given formula. PLE
	 * removes clauses that contain a literal which has the same polarisation in all
	 * clauses it occurs in. The literal's variable is then assigned a value
	 * according to the literals polarisation, causing all clauses that contain the
	 * literal to become TRUE. This way PLE can greatly reduce the number of clauses
	 * in formula.
	 *
	 * @param cnfFormula the formula that is simplified using PLE
	 * @return the assignment that contains the values that where set to the
	 *         variables during PLE
	 */
	public Optional<Assignment> ple(CnfFormula cnfFormula) {
		LOG.trace("Simplifying formula using pure literal elimination {}", cnfFormula);
		Set<Variable> variables = DpllSolver.allVariables;
		Assignment ass = new Assignment();
		//cnfFormula.setOperands(simplifier.simplify(cnfFormula, ass).getOperands());
		for (Iterator<Variable> varIterator = variables.iterator(); varIterator.hasNext();) {
			Variable variable = varIterator.next();
			VariableMapping varmap = hasSameFlag(variable, cnfFormula);
			if (varmap.isValid()) {
				ass.setValue(variable, varmap.getPolarity());
				cnfFormula.setOperands(simplifier.simplify(cnfFormula, ass).getOperands());
			}
		}
		if (ass.getVariables().isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(ass);
	}

	private VariableMapping hasSameFlag(Variable var, CnfFormula cnfFormula) {
		boolean polarity = false;
		boolean initialized = false;

		Set<Or<Atom>> clauses = cnfFormula.getOperands();

		for (Iterator<Or<Atom>> clausesIterator = clauses.iterator(); clausesIterator.hasNext();) {
			Or<Atom> clause = clausesIterator.next();
			Set<Literal> literals = clause.getLiterals();
			for (Iterator<Literal> iterator = literals.iterator(); iterator.hasNext();) {
				Literal literal = iterator.next();
				if (literal.getVariable().getName().equals(var.getName())) {
					if (!initialized) {
						polarity = literal.isPositive();
						initialized = true;
					} else if (polarity != literal.getPolarity().isPositive()) {
						return new VariableMapping(polarity, false);
					}
				}
			}
		}
		return new VariableMapping(polarity, initialized);
	}

}

class VariableMapping {

	private boolean polarity;
	private boolean valid;

	public VariableMapping(boolean polarity, boolean valid) {
		this.polarity = polarity;
		this.valid = valid;
	}

	public boolean getPolarity() {
		return polarity;
	}

	public void setPolarity(boolean polarity) {
		this.polarity = polarity;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
