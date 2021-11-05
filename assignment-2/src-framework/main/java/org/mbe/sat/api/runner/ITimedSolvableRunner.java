package org.mbe.sat.api.runner;

import org.mbe.sat.api.solver.ISolver;

/**
 * Solves a problem using an {@link ISolver} and measures the execution time. The result of this timed run contains the
 * result of the {@link ISolver}, as well as the duration of its execution.
 *
 * @param <P> the parameter type that is used for the problem that is given to the {@link ISolver}
 * @param <R> the return type of the {@link ISolver}
 * @param <T> the return type of the timed run method
 */
public interface ITimedSolvableRunner<P, R, T> {

    /**
     * Runs the given solver on the given problem and measures the execution time of the solving.
     *
     * @param solver the solver that is used to solve the problem
     * @param problem the problem that is solved by the solver
     * @return the result of the solving and the time it took
     */
    T runTimed(ISolver<P, R> solver, P problem);
}
