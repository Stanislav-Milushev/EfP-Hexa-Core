package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.SolutionSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DpllSolver implements ISolver<CnfFormula, Optional<Assignment>> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DpllSolver.class);

    protected ISimplifier<CnfFormula, Assignment> simplifier;

    /**
     * Creates a new instance of {@link DpllSolver}.
     */
    public DpllSolver() {
        // Since this assignment depends on assignment 2, you may choose to
        // use your own simplifier instead of the one from the sample solution
        this.simplifier = new SolutionSimplifier();
        // this.simplifier = new YourSimplifier();
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

        return Optional.empty();
    }
}
