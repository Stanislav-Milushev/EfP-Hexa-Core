package org.mbe.sat.core.model.formula;

import java.util.Collections;
import java.util.Set;

/**
 * A constant {@link Formula formula} that represents a single value which is independent from any assignment. An
 * instance of a constant will always return the same result, independent from the given assignment. It also has no
 * variables or literals.
 *
 * @param <T> the type of the value this constant holds
 */
public abstract class Constant<T> extends Atom {

    /**
     * The value of this constant.
     */
    private final T value;

    /**
     * Creates a new constant formula with the given value. After creation the constant's value can no longer be
     * changed.
     *
     * @param value the value of the constant
     */
    protected Constant(T value) {
        this.value = value;
    }

    /**
     * Returns the value of the constant.
     *
     * @return the value of the constant
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns an empty list since a constants does not contain any variables.
     *
     * @return an empty list
     */
    @Override
    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    /**
     * Returns an empty list since a constants does not contain any literals.
     *
     * @return an empty list
     */
    @Override
    public Set<Literal> getLiterals() {
        return Collections.emptySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constant<?> constant = (Constant<?>) o;

        return getValue().equals(constant.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
