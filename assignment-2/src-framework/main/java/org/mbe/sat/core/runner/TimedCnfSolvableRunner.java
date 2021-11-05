package org.mbe.sat.core.runner;

import org.mbe.sat.api.runner.ITimedSolvableRunner;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

import java.util.Optional;

/**
 * Runner that solves a {@link CnfFormula} formula using an {@link ISolver} and returns a result containing the execution time
 * and a satisfying {@link Assignment}, if the CNF is satisfiable.
 */
public class TimedCnfSolvableRunner implements ITimedSolvableRunner<CnfFormula, Optional<Assignment>, TimedCnfSolvableRunner.TimedResult<Optional<Assignment>>> {

    /**
     * Tries to solve the given CNF with the given solver and measures the execution time. If the CNF is satisfiable,
     * the result contains an {@link Optional} of the satisfying {@link Assignment}, otherwise the optional is empty.
     *
     * @param solver the solver that is used to solve the problem
     * @param cnfFormula    the CNF that is checked for satisfiability
     * @return the result containing the execution time in milliseconds as well as an optional satisfying assignment if
     * the given CNF is satisfiable
     */
    @Override
    public TimedResult<Optional<Assignment>> runTimed(ISolver<CnfFormula, Optional<Assignment>> solver, CnfFormula cnfFormula) {

        // Measure time before and after the run
        long executionTimeStart = System.currentTimeMillis();
        Optional<Assignment> solvingResult = solver.solve(cnfFormula);
        long executionTimeEnd = System.currentTimeMillis();

        // Calculate total runtime and return a timed result
        long executionTime = executionTimeEnd - executionTimeStart;
        return TimedResult.from(solvingResult, executionTime);
    }

    /**
     * Represents the result of a timed solving process containing the execution time and the result of the solving
     * process.
     *
     * @param <T> the type of the result
     */
    public static class TimedResult<T> {

        /**
         * The result that the solving process created.
         */
        private final T result;

        /**
         * The time that the solving process took to execute.
         */
        private final long durationInMilliseconds;

        /**
         * Creates a new instance of {@link TimedResult} with the given result and duration.
         *
         * @param result                 the result of the solving process
         * @param durationInMilliseconds the duration of the solving process
         */
        public TimedResult(T result, long durationInMilliseconds) {
            this.result = result;
            this.durationInMilliseconds = durationInMilliseconds;
        }

        /**
         * Static factory method to create a new instance of {@link TimedResult} with the given result and duration.
         *
         * @param result                 the result of the solving process
         * @param durationInMilliseconds the duration of the solving process
         * @see #TimedResult(T, long)
         */
        public static <T> TimedResult<T> from(T result, long durationInMilliseconds) {
            return new TimedResult<>(result, durationInMilliseconds);
        }

        /**
         * Returns the actual result of the solving process.
         *
         * @return the actual result of the solving process
         */
        public T getResult() {
            return result;
        }

        /**
         * Returns the duration of the solving process in milliseconds.
         *
         * @return the duration of the solving process in milliseconds
         */
        public long getDurationInMilliseconds() {
            return durationInMilliseconds;
        }

        @Override
        public String toString() {
            return String.format("Satisfiable=%s, duration=%s", result, getDurationAsString());
        }

        /**
         * Returns the duration as string consisting of the time in milliseconds and full seconds.
         *
         * @return the duration as string consisting of the time in milliseconds and full seconds
         */
        public String getDurationAsString() {
            return String.format("%sms (%ss)", durationInMilliseconds, durationInMilliseconds / 1000);
        }
    }
}
