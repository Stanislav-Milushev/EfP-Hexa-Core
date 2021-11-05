package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a variable of a {@link Formula}. A variable itself cannot have a value, but it can evaluate to a boolean
 * value for a given {@link Assignment}. This class implements {@link Comparable} and defines two variables as equal if
 * they have the same name.
 */
public class Variable extends Formula implements Comparable<Variable> {

    /**
     * The name of the variable, usually just an uppercase letter like 'A' or 'B'.
     */
    private final String name;

    /**
     * Creates a new instance of {@link Variable} with the given name.
     *
     * @param name the name of the created variable
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Copy constructor that creates a new instance of {@link Variable} by creating a duplicate of the given variable.
     *
     * @param variable the variable that is copied to create a new instance
     */
    public Variable(Variable variable) {
        this.name = variable.getName();
    }

    /**
     * Factory method to create a new instance of {@link Variable} with the given name.
     *
     * @param name the name of the created variable
     */
    public static Variable fromName(String name) {
        return new Variable(name);
    }

    /**
     * Returns the name of the variable.
     *
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        return getName() != null ? getName().equals(variable.getName()) : variable.getName() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Variable otherVariable) {
        return name.compareTo(otherVariable.getName());
    }

    /**
     * Returns the name of the variable.
     *
     * @return the name of the variable
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a list that only contains the variable itself.
     *
     * @return a list that only contains the variable itself
     */
    @Override
    public Set<Variable> getVariables() {
        return Collections.singleton(this);
    }

    /**
     * Returns an empty list, since a variable cannot consist of literals.
     *
     * @return an empty list
     */
    @Override
    public Set<Literal> getLiterals() {
        return Collections.emptySet();
    }

    /**
     * Returns the value that is assigned to the variable in the given assignment. If the assignment does not cover the
     * variable, we cannot evaluate it and return an {@link Tristate#UNDEFINED UNDEFINED} tristate.
     *
     * @param assignment the assignment that is used to evaluate the variable
     * @return the value of the variable in the given assignment
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {

        // Only if the variable is covered by the assignment we can get its value from it. If the assignment does not
        // cover the variable, we cannot evaluate it and return an undefined Tristate.
        if (assignment.covers(this)) {
            return assignment.getValue(this);
        } else {
            return Tristate.UNDEFINED;
        }
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        switch (assignment.getValue(this)) {
            case FALSE:
                return False.create();
            case TRUE:
                return True.create();
            default:
                // Create a copy of the variable
                return new Variable(this);
        }
    }
}
