package org.mbe.sat.impl.sampler;

import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.solver.DpllSolver;

public class BasicTwoWiseCombinatorialSamplerTest extends AbstractSamplerTest {

    @Override
    public ISampler<CnfFormula, Assignment> getSampler() {
        return new BasicTwoWiseCombinatorialSampler(new DpllSolver());
    }
}