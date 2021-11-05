package org.mbe.sat.core.model.formula;

import org.mbe.sat.core.model.Assignment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Formula that represents a boolean disjunction, where {@link T} specifies the type of sub-formulas that the
 * disjunction consists of.
 *
 * @param <T> the type of formulas the disjunction consists of
 */
public class Or<T extends Formula> extends NAryOperator<T> {

    /**
     * Creates a new instance of an {@link Or} with the given collection of operands.
     *
     * @param operands the list of operands the created {@link Or disjunction} will consist of
     */
    public Or(Set<T> operands) {
        super(operands);
    }

    /**
     * Copy constructor that creates a new instance of an {@link Or} as copy of the given one.
     *
     * @param or the disjunction that is copied
     */
    public Or(Or<T> or) {
        this(new HashSet<>(or.getOperands()));
    }

    /**
     * Creates a new instance of an {@link Or Or} with the given two operands of the disjunction.
     *
     * @param firstOperand  the first operand the created {@link Or disjunction} will consist of
     * @param secondOperand the second operand the created {@link Or disjunction} will consist of
     */
    public Or(T firstOperand, T secondOperand) {
        super(Stream.of(firstOperand, secondOperand).collect(Collectors.toSet()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tristate isSatisfiedWith(Assignment assignment) {
        if (assignment.covers(this)) {

            // The evaluation result of a disjunction is true, if the evaluation of any operands is true
            return Tristate.of(operands.stream().anyMatch(t -> t.isSatisfiedWith(assignment).isTrue()));
        } else {
            // If not all operands are covered by the assignment, we must return UNDEFINED
            return Tristate.UNDEFINED;
        }
    }

    @Override
    public Formula evaluate(Assignment assignment) {
        Set<Formula> collect = getOperands().stream()
                .map(t -> t.evaluate(assignment))
                .collect(Collectors.toSet());
        return new Or<>(collect);
    }

    @Override
    public String toString() {
        return "{" + operands.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}";
    }
}
