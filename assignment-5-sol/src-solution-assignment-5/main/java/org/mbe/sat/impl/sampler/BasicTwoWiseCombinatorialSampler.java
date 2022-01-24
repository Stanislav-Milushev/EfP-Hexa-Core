package org.mbe.sat.impl.sampler;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

import java.util.Optional;

/**
 * Implementation of the {@link BasicKWiseCombinatorialSampler} that considers  2-way feature combinations.
 */
public class BasicTwoWiseCombinatorialSampler extends BasicKWiseCombinatorialSampler {

    /**
     * Creates a new instance of {@link BasicTwoWiseCombinatorialSampler} with the given solver that is used.
     *
     * @param solver the given solver that is used to calculate the samples
     */
    public BasicTwoWiseCombinatorialSampler(ISolver<CnfFormula, Optional<Assignment>> solver) {
        super(solver, 2);
    }
}
