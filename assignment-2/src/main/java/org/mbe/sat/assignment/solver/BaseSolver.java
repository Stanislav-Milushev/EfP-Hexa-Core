package org.mbe.sat.assignment.solver;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BaseSolver implements ISolver<CnfFormula, Optional<Assignment>> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BaseSolver.class);

    @Override
    public Optional<Assignment> solve(CnfFormula formula) {
        LOG.debug("Solving formula '{}' using '{}'", formula, getClass().getSimpleName());

        // Return an empty Optional, if no satisfying assignment was found.
        return Optional.empty();
    }
}
