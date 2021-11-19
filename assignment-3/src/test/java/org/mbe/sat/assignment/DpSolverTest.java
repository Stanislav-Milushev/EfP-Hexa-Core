package org.mbe.sat.assignment;


import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.problem.SatProblemJsonModel;
import org.mbe.sat.impl.solver.AbstractSolverTest;

import java.util.Optional;

public class DpSolverTest extends AbstractSolverTest {
	
    @Override
    public SatProblemJsonModel.Complexity getComplexity() {
        return SatProblemJsonModel.Complexity.MEDIUM;
    }

    @Override
    public ISolver<CnfFormula, Optional<Assignment>> getSolver() {
        return new DpSolver();
    }
}