package org.mbe.sat.impl.solver;

import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.procedure.IUnitPropagation;
import org.mbe.sat.assignment.procedure.Simplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.BatchedPureLiteralElimination;
import org.mbe.sat.impl.procedure.BatchedUnitPropagation;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

import java.util.Optional;

/**
 * Implementation of the {@link AbstractDpSolver DP algorithm} that uses {@link BatchedPureLiteralElimination Pure
 * Literal Elimination (PLE)} as the first and {@link BatchedUnitPropagation Unit Propagation (UP)} as the second
 * simplification technique to simplify the formula in each step, before making the next recursive call. For a single
 * call of {@link #simplifyFormula(CnfFormula, Assignment) simplifyFormula} the procedures PLE and UP are executed as
 * long as possible, until no more simplifications can be made.
 *
 * @see <a href="https://en.wikipedia.org/wiki/DPLL_algorithm">https://en.wikipedia.org/wiki/DPLL_algorithm</a>
 */
public class DpllSolver extends AbstractDpSolver {

    protected final ISimplifier<CnfFormula, Assignment> simplifier;
    protected final IUnitPropagation<CnfFormula, Assignment> unitPropagation;
    protected final IPureLiteralElimination<CnfFormula, Assignment> pureLiteralElimination;

    public DpllSolver() {
        this.simplifier = new SolutionSimplifier();
        this.unitPropagation = new BatchedUnitPropagation(simplifier);
        this.pureLiteralElimination = new BatchedPureLiteralElimination(simplifier);
    }

    /**
     * Simplifies the given formula by evaluating it with the given assignment and then calling {@link
     * Simplifier#simplify(CnfFormula, Assignment) SIMPLIFY} once. Afterwards the simplification procedures {@link
     * BatchedPureLiteralElimination#ple(CnfFormula) PLE} and {@link BatchedUnitPropagation#up(CnfFormula) UP} are
     * called as long as possible, until no more simplifications can be made. Each call of PLE and UP already includes a
     * call of SIMPLIFY. Therefore no direct SIMPLIFY call is present after PLE and UP have finished.
     *
     * @param cnfFormula    the formula that is simplified
     * @param newAssignment the assignment that was created during the last iteration and is updated in this method
     *                      eventually
     * @return the simplified formula
     */
    @Override
    protected CnfFormula simplifyFormula(CnfFormula cnfFormula, Assignment newAssignment) {

        // Evaluate and simplify the given CNF formula with the given assignment
        CnfFormula simplifiedCnfFormula = simplifier.simplify(cnfFormula, newAssignment);

        // Now we call PLE and UP as long as possible. Each time PLE or UP simplify the formula successfully, the
        // boolean variable "shouldContinue" is reset to true. This way the while-loop only ends, if both procedures
        // can no longer perform any simplification.
        boolean shouldContinue = true;

        while (shouldContinue) {
            shouldContinue = false;

            // When using unit propagation we must remember the assignment for the unit clauses that where propagated.
            // So we merge the assignment that was returned by the UP with the given one
            Optional<Assignment> optionalUnitPropagationAssignment = unitPropagation.up(simplifiedCnfFormula);
            if (optionalUnitPropagationAssignment.isPresent()) {
                newAssignment.mergeDistinct(optionalUnitPropagationAssignment.get());
                shouldContinue = true;
            }

            // When using pure literal elimination we must remember the assignment for the pure literals. So we merge
            // the assignment that was returned by PLE with the given one
            Optional<Assignment> optionalPleAssignment = pureLiteralElimination.ple(simplifiedCnfFormula);
            if (optionalPleAssignment.isPresent()) {
                newAssignment.mergeDistinct(optionalPleAssignment.get());
                shouldContinue = true;
            }
        }

        return simplifiedCnfFormula;
    }
}
