package org.mbe.sat.impl.solver;

import org.mbe.assignment.sat.DpllSolver;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.problem.SatProblemJsonModel;

import java.util.Optional;


class DpllSolverTest extends AbstractSolverTest {

    @Override
    public SatProblemJsonModel.Complexity getComplexity() {
        return SatProblemJsonModel.Complexity.MEDIUM;
    }

    @Override
    public ISolver<CnfFormula, Optional<Assignment>> getSolver() {
        return new DpllSolver();
    }
}