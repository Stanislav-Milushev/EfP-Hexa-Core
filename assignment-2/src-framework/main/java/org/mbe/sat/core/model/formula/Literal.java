package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.Collections;
import java.util.Set;

/**
 * An atomic {@link Formula} that is just an atom ({@link Variable}) or its negation. Whether the atom is negated is
 * defined by the literals {@link Polarity}. If the variable is negated, the polarity is <code>false</code>, or
 * <code>true</code> if it is not negated.
 */
public class Literal extends Atom {

    /**
     * Positive or negative depending on whether it is a positive or negative literal. If the variable is negated, the
     * polarity is <code>false</code>, or <code>true</code> if it is not negated.
     */
    private final Polarity polarity;

    /**
     * The atomic variable of this literal.
     */
    private final Variable variable;

    /**
     * Creates a new instance of {@link Literal} with the given {@link Variable} and a positive polarity.
     *
     * @param variable the variable of the created literal
     */
    public Literal(Variable variable) {
        this(variable, Polarity.POSITIVE);
    }

    /**
     * Copy constructor that creates a new instance of {@link Literal} by creating a duplicate of the given literal.
     *
     * @param literal the literal that is copied to create a new instance
     */
    public Literal(Literal literal) {
        this(literal.getVariable(), literal.getPolarity());
    }

    /**
     * Creates a new instance of {@link Literal} with the given {@link Polarity} and {@link Variable}.
     *
     * @param variable the variable of the created literal
     * @param polarity the polarity of the created literal
     */
    public Literal(Variable variable, Polarity polarity) {
        this.polarity = polarity;
        this.variable = variable;
    }

    /**
     * Creates a new instance of {@link Literal} with the given {@link Variable} and polarity given as boolean.
     *
     * @param variable the variable of the created literal
     * @param polarity the polarity of the created literal
     */
    public Literal(Variable variable, boolean polarity) {
        this.polarity = Polarity.of(polarity);
        this.variable = variable;
    }

    /**
     * Returns the polarity of the literal, which indicates whether the literal is positive or negative. To check if the
     * literal is positive or negative you can also use the convenience methods {@link #isNegative} and {@link
     * #isPositive()}
     *
     * @return the polarity of the literal
     */
    public Polarity getPolarity() {
        return polarity;
    }

    /**
     * Returns the variable of this literal.
     *
     * @return the variable of this literal
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * Convenience method to get the polarity. Abbreviation of <code>getPolarity().equals(Polarity.NEGATIVE)</code>
     *
     * @return <code>true</code>, if the polarity is negative, <code>false</code> otherwise.
     */
    public boolean isNegative() {
        return getPolarity().equals(Polarity.NEGATIVE);
    }

    /**
     * Convenience method to get the polarity. Abbreviation of <code>getPolarity().equals(Polarity.POSITIVE)</code>
     *
     * @return <code>true</code>, if the polarity is positive, <code>false</code> otherwise.
     */
    public boolean isPositive() {
        return getPolarity().equals(Polarity.POSITIVE);
    }

    /**
     * Returns a list that only contains the variable of the literal, since a literal can only consist of one variable.
     *
     * @return a list that only contains the variable of the literal
     */
    @Override
    public Set<Variable> getVariables() {
        // A literals does only contain one variable
        return Collections.singleton(variable);
    }

    /**
     * Returns a list that only contains the literal itself.
     *
     * @return a list that only contains the literal itself
     */
    @Override
    public Set<Literal> getLiterals() {
        return Collections.singleton(this);
    }

    /**
     * Returns the value of the variable in the given assignment or its inverted value, if the polarity {@link
     * #isNegative() is negative}. If the assignment does not cover the variable, we cannot evaluate the literals and
     * return an {@link Tristate#UNDEFINED UNDEFINED} tristate.
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {
        // Only if the literal covered by the assignment we can check if it is contained in the positive or negative
        // variables list. If the assignment does not cover the literal, we cannot evaluate it and return an undefined
        // Tristate
        if (assignment.covers(this)) {
            if (isPositive()) {
                return assignment.getValue(variable);
            } else {
                // If the literal is negative, the result is inverted
                return assignment.getValue(variable).invert();
            }
        } else {
            return Tristate.UNDEFINED;
        }
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        switch (isSatisfiedWith(assignment)) {
            case FALSE:
                return False.create();
            case TRUE:
                return True.create();
            default:
                return new Literal(this);
        }
    }

    /**
     * Returns the name of the literal's variable, prepended with a '-' if the polarity {@link #isNegative() is
     * negative}. If the literals contains the variable 'A' and has a negative polarity, this method will return the
     * string "-A".
     */
    @Override
    public String toString() {
        String literalString = getVariable().getName();

        // Prepend a '-' to the variable name, if the literal is negated
        if (isNegative()) {
            literalString = "-".concat(literalString);
        }

        return literalString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Literal literal = (Literal) o;

        if (getPolarity() != literal.getPolarity()) return false;
        return getVariable().equals(literal.getVariable());
    }

    @Override
    public int hashCode() {
        int result = getPolarity().hashCode();
        result = 31 * result + getVariable().hashCode();
        return result;
    }

    /**
     * An enumeration that defines possible values for the polarity of a variable. A polarity can be {@link #POSITIVE}
     * or {@link #NEGATIVE}. It provides convenience methods to check its state:
     * <ul>
     *     <li>{@link Polarity#isPositive()}: Returns <code>true</code> if and only if the polarity is equal to
     *     {@link Polarity#POSITIVE Polarity.POSITIVE}</li>
     *     <li>{@link Polarity#isNegative()}: Returns <code>true</code> if and only if the polarity is equal to
     *     {@link Polarity#NEGATIVE Polarity.NEGATIVE}</li>
     * </ul>
     */
    public enum Polarity {

        /**
         * Indicates that the polarity is positive, meaning that its formula is not negated.
         */
        POSITIVE {
            @Override
            public boolean isPositive() {
                return true;
            }

            @Override
            public boolean isNegative() {
                return false;
            }
        },

        /**
         * Indicates that the polarity is negative, meaning that its formula is negated.
         */
        NEGATIVE {
            @Override
            public boolean isPositive() {
                return false;
            }

            @Override
            public boolean isNegative() {
                return true;
            }
        };

        /**
         * Returns <code>true</code>, if and only if, the polarity is <code>{@link #POSITIVE}</code>.
         *
         * @return <code>true</code>, if the polarity is {@link #POSITIVE}, <code>false</code> otherwise
         */
        public abstract boolean isPositive();

        /**
         * Returns <code>true</code>, if and only if, the polarity is <code>{@link #NEGATIVE}</code>.
         *
         * @return <code>true</code>, if the polarity is {@link #NEGATIVE}, <code>false</code> otherwise
         */
        public abstract boolean isNegative();

        /**
         * Creates a {@link Polarity} constant for the given boolean value.
         *
         * @param value the boolean value that is represented by the returned polarity
         * @return {@link Polarity#POSITIVE} if the given boolean is <code>true</code> or {@link Polarity#NEGATIVE} if
         * the given boolean is <code>false</code>
         */
        public static Polarity of(boolean value) {
            return value ? POSITIVE : NEGATIVE;
        }
    }
}
