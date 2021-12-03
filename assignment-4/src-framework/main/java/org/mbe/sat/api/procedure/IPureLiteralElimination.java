package org.mbe.sat.api.procedure;

import java.util.Optional;

/**
 * Performs simplifications on formulas and clauses by removing clauses that contain a literal which has the same
 * polarisation in all clauses it occurs in. These literals are called pure literals. When simplifying a formula with
 * pure literal elimination (PLE), a pure literal can be set according to its polarization, causing all clauses it
 * occurs in to evaluate to TRUE. Thus these clauses can be removed. PLE must remember and return the values that where
 * assigned to the pure literals.
 */
public interface IPureLiteralElimination<F, A> {

    /**
     * Performs the simplification procedure "Pure Literal Elimination" (PLE) on the given formula. This method works
     * in-place and modifies the given formula. PLE removes clauses that contain a literal which has the same
     * polarisation in all clauses it occurs in. The literal's variable is then assigned a value according to the
     * literals polarisation, causing all clauses that contain the literal to become TRUE. The values assigned to the
     * variables must be collected and returned.
     *
     * @param formula the formula that is simplified using PLE
     * @return the assignment that contains the values that where set to the variables during PLE
     */
    Optional<A> ple(F formula);
}
