package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

/**
 * Formula that represents the boolean constant <code>false</code>. Given any assignment, this formula always evaluates
 * to <code>false</code>.
 */
public class False extends Constant<Boolean> {

    /**
     * Creates a new instance of {@link False}.
     */
    public False() {
        super(Boolean.FALSE);
    }

    /**
     * Factory method to create a new instance of {@link False}.
     *
     * @return a new instance of the constant {@link False} formula
     */
    public static False create() {
        return new False();
    }

    /**
     * Always returns the constant {@link Tristate#FALSE}, independent from the given assignment.
     *
     * @param assignment is ignored since a constant does always evaluate to its constant value
     * @return the tristate {@link Tristate#FALSE FALSE}
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {
        return Tristate.FALSE;
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        return create();
    }

    /**
     * Returns the string "FALSE"
     *
     * @return the string "FALSE"
     */
    @Override
    public String toString() {
        return "FALSE";
    }
}
