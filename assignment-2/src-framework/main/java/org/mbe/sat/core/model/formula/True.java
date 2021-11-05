package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

/**
 * Formula that represents the boolean constant <code>true</code>. Given any assignment, this formula always evaluates
 * to <code>true</code>.
 */
public class True extends Constant<Boolean> {

    /**
     * Creates a new instance of {@link False}.
     */
    public True() {
        super(Boolean.TRUE);
    }

    /**
     * Factory method to create a new instance of {@link True}.
     *
     * @return a new instance of the constant {@link True} formula
     */
    public static True create() {
        return new True();
    }

    /**
     * Always returns the constant {@link Tristate#TRUE}, independent from the given assignment.
     *
     * @param assignment is ignored since a constant does always evaluate to its constant value
     * @return the tristate {@link Tristate#TRUE TRUE}
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {
        return Tristate.TRUE;
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        return create();
    }

    /**
     * Returns the string "TRUE"
     *
     * @return the string "TRUE"
     */
    @Override
    public String toString() {
        return "TRUE";
    }
}
