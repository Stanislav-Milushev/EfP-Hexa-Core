package org.mbe.sat.impl;

import com.google.common.collect.Lists;
import org.mbe.sat.api.ICombinationProvider;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.SequentialCombinationProvider;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Basic SAT solver implementation that simply checks every possible assignment
 * of the variables, but performs simplification steps to minimize the number of
 * clauses and literals that have to be evaluated.
 */
public class BaseSolver implements ISolver<CnfFormula, Optional<Assignment>> {

	/**
	 * Static logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaseSolver.class);
	/**
	 * {@link ISimplifier simplifier} instance
	 */
	private final ISimplifier<CnfFormula, Assignment> simplifier;
	/**
	 * if set, {@link #solve(CnfFormula) solve} returns
	 */
	private boolean terminate;

	public BaseSolver() {
		this.simplifier = new SolutionSimplifier();
		this.terminate = false;
	}

	/**
	 * Checks if the given CNF formula is satisfiable, by finding an assignment that
	 * satisfies the formula. It does so by checking every possible assignment
	 * combination for the variables.
	 *
	 * @param formula the formula that is solved
	 * @return an {@link Optional} assignment that satisfies the formula or an empty
	 *         optional of the formula is not satisfiable
	 */
	@Override
	public Optional<Assignment> solve(CnfFormula formula) {
		LOG.debug("Solving formula '{}' using '{}'", formula, getClass().getSimpleName());

		// Check trivial cases to check if formula is satisfiable
		if (formula.isEmpty()) {
			// If the formula is empty, it is satisfiable
			System.out.println("If the formula is empty, it is satisfiable");
			return Optional.of(Assignment.empty());
		} else if (formula.containsEmptyClause()) {
			// If the formula contains an empty clause, it is NOT satisfiable
			System.out.println("If the formula contains an empty clause, it is NOT satisfiable");
			return Optional.empty();
		}

		// Create a combination provided for all variables in the formula. This
		// provider generates all possible combinations for the given variables.
		List<Variable> variables = Lists.newArrayList(formula.getVariables());
		ICombinationProvider sequentialCombinationProvider = null;
		try {
			sequentialCombinationProvider = new SequentialCombinationProvider(variables.size());
		} catch (NumberFormatException e) {
			this.terminate = false;
			return Optional.empty();
		}

		// We need to create an assignment for each possible combination of the given
		// variables. So we create all
		// possible combinations of a boolean array with the size of the number of
		// variables
		while (sequentialCombinationProvider.hasNext()) {

			if (this.terminate) {
				this.terminate = false;
				return Optional.empty();
			}

			boolean[] nextCombination = sequentialCombinationProvider.next();
			CnfFormula wipFormula = new CnfFormula(new ArrayList<>(formula.getOperands()));

			// Get the next assignment and check if the formula evaluates to TRUE
			Assignment assignment = Assignment.from(variables, nextCombination);

			// Simplify the CNF to minimize the evaluation effort
			CnfFormula simplifiedFormula = simplifier.simplify(wipFormula, assignment);

			// Return the satisfying assignment if the formula was minimized to the empty
			// formula
			if (simplifiedFormula.isEmpty()) {
				return Optional.of(assignment);
			}
		}

		this.terminate = false;
		// If the loop finished no satisfying assignment was found
		System.out.println("If the loop finished no satisfying assignment was found");
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(boolean choice) {
		this.terminate = choice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTerminated() {
		return this.terminate;
	}
}
