package org.mbe.sat.impl.procedure.up;

import org.mbe.assignment.sat.UnitPropagation;
import org.mbe.sat.api.procedure.IUnitPropagation;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.SolutionSimplifier;

public class UnitPropagationTest extends AbstractUnitPropagationTest {
    @Override
    protected IUnitPropagation<CnfFormula, Assignment> getUnitPropagation() {
        // For testing we use the sample solution simplifier. Make sure that you test your UP implementation with the
        // same simplifier you use in the DPLL implementation.
        return new UnitPropagation(new SolutionSimplifier());
    }
}
