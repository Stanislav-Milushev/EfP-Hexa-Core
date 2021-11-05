package org.mbe.sat.api.procedure;

/**
 * Performs simplifications on formulas when an assignment and a formula is given. If a clause contains a TRUE constant
 * after it was evaluated with the assignment, the clause is already satisfied and removed from the formula. In case a
 * clause contains a FALSE constant, this constant is removed because it cannot contribute to the satisfiability.
 *
 * @param <F> the formula in CNF that is simplified
 * @param <A> the assignment that is used to evaluate the formula before it is simplified
 */
public interface ISimplifier<F, A> {

    /**
     * simplifies the given formula by evaluating it with the given assignment and then performing simplification steps.
     * Clauses that contain a TRUE constant after the formula was evaluated, are removed from the formula. In case a
     * clause contains a FALSE constant, this constant is removed because it cannot contribute to the satisfiability.
     * The given formula is not modified, instead a copy is returned that contains the simplified formula.
     *
     * @param formula    the formula in CNF that is simplified
     * @param assignment the assignment that is used to evaluate the formula
     */
    F simplify(F formula, A assignment);
}