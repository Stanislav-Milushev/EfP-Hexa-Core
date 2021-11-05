package org.mbe.sat.assignment.procedure;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simplifier implements ISimplifier<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Simplifier.class);

    public CnfFormula simplify(CnfFormula cnfFormula, Assignment assignment) {
        LOG.debug("Simplifying formula '{}' with assignment '{}'", cnfFormula, assignment);

        CnfFormula evaluatedFormula = cnfFormula.evaluate(assignment);
        // TODO: Simplify evaluated formula

        return evaluatedFormula;
    }
}
