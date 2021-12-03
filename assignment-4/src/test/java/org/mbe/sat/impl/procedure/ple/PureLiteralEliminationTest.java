package org.mbe.sat.impl.procedure.ple;

import org.mbe.assignment.sat.PureLiteralElimination;
import org.mbe.sat.api.procedure.IPureLiteralElimination;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

public class PureLiteralEliminationTest extends AbstractPureLiteralEliminationTest {

    @Override
    protected IPureLiteralElimination<CnfFormula, Assignment> getPureLiteralElimination() {
        // For testing we use the sample solution simplifier. Make sure that you test your PLE implementation with the
        // same simplifier you use in the DPLL implementation.
        return new PureLiteralElimination(new SolutionSimplifier());
    }
}
