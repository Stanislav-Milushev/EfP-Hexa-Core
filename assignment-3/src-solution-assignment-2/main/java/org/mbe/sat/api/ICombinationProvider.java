package org.mbe.sat.api;

import java.util.NoSuchElementException;

/**
 * Provides all combinations of boolean arrays with limited size. For an array of size 'n' there are 2^n possible
 * combinations. When the size is '3' the method {@link #next()} will provide the following boolean arrays:
 * <p>
 * <code>
 * [true, true, true],<br>[true, true, false],<br>[true, false, true], <br>..., <br>[false, false, false]
 * </code>
 * </p>
 * No combination is returned multiple times, so {@link #next()} can be called 2^3 times before it throws a {@link
 * NoSuchElementException}.
 */
public interface ICombinationProvider {

    /**
     * Returns true if the provider has more combinations. (In other words, returns true if {@link #next()} would return
     * an element rather than throwing an exception.)
     *
     * @return <code>true</code> if the provider has more combinations
     */
    boolean hasNext();

    /**
     * Returns the next combination.
     *
     * @return the next combination
     *
     * @throws NoSuchElementException if the provider has no more combinations
     */
    boolean[] next();
}
