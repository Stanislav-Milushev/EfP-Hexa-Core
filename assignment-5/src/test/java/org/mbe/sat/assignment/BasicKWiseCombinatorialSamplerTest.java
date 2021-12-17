package org.mbe.sat.assignment;

import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.impl.sampler.AbstractSamplerTest;

public class BasicKWiseCombinatorialSamplerTest extends AbstractSamplerTest {

    @Override
    public ISampler<CnfFormula, Assignment> getSampler() {
        return new BasicKWiseCombinatorialSampler();
    }
}
