package org.mbe.sat.api.solver;

/**
 * Solves the given formula, by finding a satisfying assignment.
 *
 * @param <P> the type that is used as parameter for the formula
 * @param <R> the type that is used as return value for the solving result
 */
public interface ISolver<P, R> {

    /**
     * Solves the given formula, by finding a satisfying assignment.
     *
     * @param formula the formula that is solved
     * @return the result of the solving process
     */
    R solve(P formula);
}
