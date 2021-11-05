package org.mbe.sat.core.model;

/**
 * Defines a SAT Problem by the number of variables and clauses as well as the {@link Format} the formula is given in.
 */
public class ProblemDefinition {

    /**
     * The {@link Format} the formula is given in.
     */
    private final Format format;
    /**
     * The number of all variables that the formula consist of. Variables with the same name are not counted twice.
     */
    private final int numberOfVariables;
    /**
     * The number of all clauses that the formula consist of.
     */
    private final int numberOfClauses;

    /**
     * Creates a new instance of a {@link ProblemDefinition} with the given format and number of variables and clauses.
     *
     * @param format            the {@link Format} the formula is given in
     * @param numberOfVariables the number of all variables that the formula consist of. Variables with the same name
     *                          are not counted twice.
     * @param numberOfClauses   the number of all clauses that the formula consist of
     */
    public ProblemDefinition(Format format, int numberOfVariables, int numberOfClauses) {
        this.format = format;
        this.numberOfVariables = numberOfVariables;
        this.numberOfClauses = numberOfClauses;
    }

    /**
     * Returns the {@link Format} the formula is given in.
     *
     * @return the {@link Format} the formula is given in
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Return the number of all variables that the formula consist of. Variables with the same name are not counted
     * twice.
     *
     * @return the number of all variables that the formula consist of
     */
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    /**
     * Returns the number of all clauses that the formula consist of.
     *
     * @return the number of all clauses that the formula consist of
     */
    public int getNumberOfClauses() {
        return numberOfClauses;
    }

    /**
     * Defines all formats that a formula can be given in. Other format except {@link #CNF} are possible, but we only
     * work with CNF formatted formulas. All other formula formats will result in the format {@link #UNKNOWN}.
     */
    public enum Format {

        /**
         * Indicates that the formula is represented using the conjunctive normal form (CNF).
         */
        CNF,

        /**
         * Indicates that the formula is represented using a format that is unknown and unsupported.
         */
        UNKNOWN;

        /**
         * Creates a format using the given string. The only supported format is {@link #CNF} and all other format
         * strings will return an {@link Format#UNKNOWN UNKNOWN formula}.
         *
         * @param formatString the string whose format enumeration constant is returned
         * @return the format enumeration constant for the given string
         */
        public static Format getByString(String formatString) {
            if ("cnf".equals(formatString)) {
                return CNF;
            }
            return UNKNOWN;
        }
    }
}
