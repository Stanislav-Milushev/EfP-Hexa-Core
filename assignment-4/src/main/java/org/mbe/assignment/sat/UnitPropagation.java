package org.mbe.assignment.sat;

import org.mbe.sat.api.procedure.ISimplifier;
import org.mbe.sat.api.procedure.IUnitPropagation;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Performs simplifications on formulas and clauses by propagating assignments for literals in clauses that only contain
 * one literal.
 */
public class UnitPropagation implements IUnitPropagation<CnfFormula, Assignment> {

    /**
     * Static logger instance for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(UnitPropagation.class);

    private final ISimplifier<CnfFormula, Assignment> simplifier;

    public UnitPropagation(ISimplifier<CnfFormula, Assignment> simplifier) {
        this.simplifier = simplifier;
    }

    /**
     * Performs the simplification procedure "Unit Propagation" (UP) on the given formula. This method works in-place
     * and modifies the given formula. UP searches for clauses that only contain one literal. In order to satisfy this
     * clause, the literal must evaluate to TRUE. So the variable is assigned a value to satisfy its clause and the
     * value is set (propagated) for all literals with the same variable in the whole formula.
     *
     * @param cnfFormula the formula that is simplified using UP
     * @return the assignment that contains the values that where set to the variables during UP
     */
    public Optional<Assignment> up(CnfFormula cnfFormula) {
        LOG.trace("Simplifying formula using unit propagation {}", cnfFormula);

        return Optional.empty();
    }
}
