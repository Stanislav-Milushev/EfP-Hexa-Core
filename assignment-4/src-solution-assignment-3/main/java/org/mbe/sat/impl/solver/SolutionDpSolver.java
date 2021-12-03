package org.mbe.sat.impl.solver;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

/**
 * Implementation of the {@link AbstractDpSolver DP Algorithm} that uses the basic simplification procedure to simplify
 * the formula in each recursive step.
 */
public class SolutionDpSolver extends AbstractDpSolver {

    private final ISimplifier<CnfFormula, Assignment> simplifier;

    public SolutionDpSolver() {
        this.simplifier = new SolutionSimplifier();
    }

    /**
     * Simplifies the given CNF formula by simplifying each clause and only adding clauses to the simplified CNF that
     * are satisfiable. The given CNF formula is not modified, instead a simplified copy of the given formula is
     * returned.
     *
     * @param formula       the formula that is simplified
     * @param newAssignment the assignment that was created during the last iteration
     * @return the formula that was created by evaluating and simplifying the given formula with the given assignment
     */
    @Override
    protected CnfFormula simplifyFormula(CnfFormula formula, Assignment newAssignment) {
        return simplifier.simplify(formula, newAssignment);
    }
}
