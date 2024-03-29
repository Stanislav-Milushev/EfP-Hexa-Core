package org.mbe.sat.assignment.solver;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.procedure.Simplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class BaseSolver implements ISolver<CnfFormula, Optional<Assignment>> {

	/**
	 * Static logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaseSolver.class);
	/**
	 * if set, {@link #solve(CnfFormula) solve} returns
	 */
	private boolean terminate;

	@Override
	public Optional<Assignment> solve(CnfFormula formula) {
		LOG.debug("Solving formula '{}' using '{}'", formula, getClass().getSimpleName());

		this.terminate = false;

		Set<Variable> variables = formula.getVariables();
		BooleanCombinator booleanCombinator = new BooleanCombinator(variables.size());
		Simplifier simplifier = new Simplifier();

		Assignment assignment = new Assignment();
		int numberOfCombinations = (int) Math.pow(2, variables.size());
		int index;

		for (int i = 0; i + 1 <= numberOfCombinations; i++) {

			if (this.terminate) {
				this.terminate = false;
				return Optional.empty();
			}

			// Reset assignment and index
			assignment = Assignment.empty();
			index = 0;

			// Get boolean combination by index
			boolean[] combination = booleanCombinator.getCombinationByBitIndex(i);

			// Loop through the formula variables and set Boolean values depending on
			// current combination
			for (Iterator<Variable> it = variables.iterator(); it.hasNext();) {
				Variable v = it.next();
				assignment.setValue(v, combination[index]);
				index++;
			}

			// If the simplify method returns an empty formula, Optional is returned with a
			// satisfying assignment
			if (simplifier.simplify(formula, assignment).isEmpty()) {
				return Optional.of(assignment);
			}

		}

		// Return an empty Optional, if no satisfying assignment was found.
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(boolean choice) {
		this.terminate = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTerminated() {
		return this.terminate;
	}

}
