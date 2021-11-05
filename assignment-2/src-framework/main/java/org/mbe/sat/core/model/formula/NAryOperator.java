package org.mbe.sat.core.model.formula;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A boolean formula that represents an n-ary boolean operator, where {@link T} specifies the type of sub-formulas that
 * the operator consists of.
 *
 * @param <T> the type of formulas the conjunction consists of
 */
public abstract class NAryOperator<T extends Formula> extends Formula {

    /**
     * All operands of this operator. Since formulas use the composite pattern, the operands can be formulas itself.
     */
    protected Set<T> operands;

    /**
     * Creates a new instance of {@link NAryOperator} with the given collection of operands.
     *
     * @param operands the list of operands the created {@link NAryOperator} will consist of
     */
    public NAryOperator(Set<T> operands) {
        this.operands = operands;
    }

    /**
     * Returns all operands of this operator.
     *
     * @return all operands of this operator
     */
    public Set<T> getOperands() {
        return operands;
    }

    public void setOperands(Set<T> operands) {
        this.operands = operands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Variable> getVariables() {
        return operands.stream().flatMap(t -> t.getVariables().stream()).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Literal> getLiterals() {
        return operands.stream().flatMap(t -> t.getLiterals().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NAryOperator<?> that = (NAryOperator<?>) o;
        return Objects.equals(getOperands(), that.getOperands());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperands());
    }
}
