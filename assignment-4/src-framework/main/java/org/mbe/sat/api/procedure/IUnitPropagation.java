package org.mbe.sat.api.procedure;

import java.util.Optional;

/**
 * Performs simplifications on formulas and clauses by propagating assignments for literals in clauses that only contain
 * one literal. If a clause does only contain one literals, the whole formula can only be satisfied if this clause
 * evaluates to TRUE. When simplifying a formula with unit propagation (UP), the literal's variable must be set
 * according to the literal's polarization to make the clause evaluate to TRUE. This variable value is then propagated
 * through the whole formula. UP must remember and return the values that where assigned to the literals.
 */
public interface IUnitPropagation<F, A> {

    /**
     * Performs the simplification procedure "Unit Propagation" (UP) on the given formula. This method works in-place
     * and modifies the given formula. UP searches for clauses that only contain one literal. In order to satisfy this
     * clause, the literal must evaluate to TRUE. So the variable is assigned a value to satisfy its clause and the
     * value is set (propagated) for all literals with the same variable in the whole formula.
     *
     * @param formula the formula that is simplified using UP
     * @return the assignment that contains the values that where set to the variables during UP
     */
    Optional<A> up(F formula);
}
