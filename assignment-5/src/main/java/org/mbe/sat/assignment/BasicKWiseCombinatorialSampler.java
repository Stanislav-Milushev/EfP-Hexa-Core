package org.mbe.sat.assignment;

import org.mbe.sat.api.sampler.ISampler;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

import java.util.Set;

public class BasicKWiseCombinatorialSampler implements ISampler<CnfFormula, Assignment> {
    
    @Override
    public Set<Assignment> sample(CnfFormula formula) {
        return null;
    }
}
