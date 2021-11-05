package org.mbe.sat.assignment.procedure;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.procedure.simplify.AbstractSimplifyTest;

class SimplifierTest extends AbstractSimplifyTest {

    @Override
    public ISimplifier<CnfFormula, Assignment> getSimplifier() {
        return new Simplifier();
    }
}