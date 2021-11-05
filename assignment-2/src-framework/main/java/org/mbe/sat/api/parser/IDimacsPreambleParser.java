package org.mbe.sat.api.parser;

import org.mbe.sat.api.IllegalPreambleFormatException;

/**
 * Used to parse a preamble formatted in the DIMACS format.
 *
 * @param <T> the type of the returned preamble
 */
public interface IDimacsPreambleParser<T> {

    /**
     * Parses the given DIMACS preamble and returns the result.
     *
     * @param preamble the DIMACS preamble that is parsed
     * @return the parsed preamble
     *
     * @throws IllegalPreambleFormatException if the preamble is not formatted correctly according to the DIMACS format
     */
    T parse(String preamble);
}
