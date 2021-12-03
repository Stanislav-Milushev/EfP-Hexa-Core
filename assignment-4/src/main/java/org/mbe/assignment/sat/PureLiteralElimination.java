package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Performs simplifications on formulas and clauses by removing clauses that contain a literal which has the same
 * polarisation in all clauses it occurs in. This implementation uses a sequential approach, meaning that it processes
 * only one pure literal at a time. For each pure literal that is found, {@link ISimplifier#simplify(Object, Object)
 * simplify} is called to to eliminate the pure literal.
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
     * Creates a new instance of the {@link PureLiteralElimination} with the given simplifier which is used to
     * eliminate the pure literals that where found in the formula
     *
     * @param simplifier simplifier which is used to eliminate the pure literals that where found in the formula
     */
    public PureLiteralElimination(ISimplifier<CnfFormula, Assignment> simplifier) {
        this.simplifier = simplifier;
    }

    /**
     * Performs the simplification procedure "Pure Literal Elimination" (PLE) on the given formula. This method works
     * in-place and modifies the given formula. PLE removes clauses that contain a literal which has the same
     * polarisation in all clauses it occurs in. The literal's variable is then assigned a value according to the
     * literals polarisation, causing all clauses that contain the literal to become TRUE. This way PLE can greatly
     * reduce the number of clauses in formula.
     *
     * @param cnfFormula the formula that is simplified using PLE
     * @return the assignment that contains the values that where set to the variables during PLE
     */
    public Optional<Assignment> ple(CnfFormula cnfFormula) {
        LOG.trace("Simplifying formula using pure literal elimination {}", cnfFormula);

        return Optional.empty();
    }
}
