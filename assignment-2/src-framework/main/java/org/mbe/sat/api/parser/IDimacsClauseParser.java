package org.mbe.sat.api.parser;

import org.mbe.sat.api.IllegalClauseFormatException;

/**
 * Used to parse clause definitions formatted in the DIMACS format.
 *
 * @param <T> the type of the returned clause
 */
public interface IDimacsClauseParser<T> {

    /**
     * Parses the given DIMACS clause and returns the result.
     *
     * @param dimacsClauseDefinition the DIMACS clause that is parsed
     * @return the parsed clause
     *
     * @throws IllegalClauseFormatException if the clause is not formatted correctly according to the DIMACS format
     */
    T parse(String dimacsClauseDefinition);
}
