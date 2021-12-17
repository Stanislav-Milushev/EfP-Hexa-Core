package org.mbe.sat.api.sampler;

import java.util.Set;

/**
 * Generates samples for a satisfiable formula. A sample is a set of valid configurations for a formula. If the formula
 * is unsatisfiable the sample is empty.
 *
 * @param <F> the type of formulas that the configurations are created for
 * @param <A> the type of configurations that are returned
 */
public interface ISampler<F, A> {

    /**
     * Generates a set of valid configurations for the given formula.
     *
     * @param formula the formula that the sample is generated for
     * @return a set of sample for the given formula, or an empty set, if the formula is not satisfiable
     */
    Set<A> sample(F formula);
}
