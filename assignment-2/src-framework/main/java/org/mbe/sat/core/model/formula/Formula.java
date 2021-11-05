package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.Set;

/**
 * Abstract superclass of all boolean formulas.
 */
public abstract class Formula {

    /**
     * Returns a list of all variables that the formula consists of. The returned list will not contain any duplicates.
     *
     * @return a list of all variables that the formula consists of
     */
    public abstract Set<Variable> getVariables();

    /**
     * Returns a list of all literals that the formula consists of. The returned list will not contain any duplicates.
     *
     * @return a list of all literals that the formula consists of
     */
    public abstract Set<Literal> getLiterals();

    /**
     * Evaluates this formula with the given assignment. If the formula is satisfied with given assignment (evaluates to
     * TRUE), this method returns <code>true</code>, or <code>false</code> otherwise.
     *
     * @param assignment the assignment that is used to check, if the assignment satisfies the formula
     * @return <code>true</code>, if the assignment satisfies the formula, or <code>false</code> otherwise
     */
    public abstract Tristate isSatisfiedWith(final Assignment assignment);

    /**
     * Evaluate the formula with the given assignment and creates a new formula that contains the result of the
     * evaluation. The original formula is not modified.
     *
     * @param assignment the assignment that is used to evaluate the formula
     * @return the formula that was created by evaluating this formula with the given assignment
     */
    public abstract Formula evaluate(final Assignment assignment);
}
