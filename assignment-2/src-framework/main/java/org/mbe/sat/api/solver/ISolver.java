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

	/**
	 * sets the terminate flag withing the {@link ISolver} instance for beeing able
	 * to abort execution of the {@link #solve(Object) solve} method
	 * 
	 * @param choice / new value of the terminate flag of the {@link ISolver}
	 *               instance
	 */
	public void terminate(boolean choice);

	/**
	 * checks wether the terminate flag within the {@link ISolver} instance is set
	 * 
	 * @return terminate flag of {@link ISolver} instance
	 */
	public boolean isTerminated();
}
